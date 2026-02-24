package com.example.demo.service.Reservation;

import com.example.demo.dto.Reservation.StoreCreateUpdateDto;
import com.example.demo.entity.Reservation.Category;
import com.example.demo.entity.Reservation.StoresInfo;
import com.example.demo.entity.User;
import com.example.demo.repository.Reservation.CategoryRepository;
import com.example.demo.repository.Reservation.StoreInfoRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreInfoService {

    private final UserRepository userRepository;
    private final StoreInfoRepository storeInfoRepository;
    private final CategoryRepository categoryRepository;

    // 取得目前登入使用者的 User 實體
    private User getCurrentUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("找不到該登入使用者"));
    }

    // 圖片儲存路徑（從設定檔讀取，若無則使用預設值）
    @Value("${app.upload.store-profile-dir:front-end/public/pictures/StoreProfile}")
    private String uploadDir;

    // 取得目前登入使用者的店家資訊
    @Transactional(readOnly = true)
    public Optional<StoresInfo> getMyStoreInfo() {
        User user = getCurrentUserEntity();
        return storeInfoRepository.findByUser_Id(user.getId());
    }

    // 更新店家資訊，預設輸入 null，表示不更新該欄位資料
    @Transactional
    public StoresInfo updateMyStoreInfo(StoreCreateUpdateDto dto) {
        User user = getCurrentUserEntity();
        StoresInfo store = storeInfoRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("找不到該店家的資訊或您不具備店家身分"));

        // 變更名稱
        if (dto.getStoreName() != null) {
            store.setStoreName(dto.getStoreName());
        }

        // 變更簡介
        if (dto.getDescription() != null) {
            store.setDescription(dto.getDescription());
        }

        // 變更電話
        if (dto.getStorePhone() != null) {
            store.setStorePhone(dto.getStorePhone());
        }

        // 變更地址(待處理地圖座標API)
        if (dto.getAddress() != null) {
            store.setAddress(dto.getAddress());
        }

        // 處理圖片邏輯：若有上傳新圖，則優先執行上傳(會覆蓋舊圖)；若無新圖但要求刪除，則執行刪除
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            handleImageUpload(store, dto.getImageFile());
        } else if (Boolean.TRUE.equals(dto.getRemoveImage())) { // 寫這樣在上傳新圖後反悔取消時才不會刪除舊有圖片
            removeExistingImage(store);
        }

        // 處理標籤邏輯：先選取標籤，再加入或刪除
        if (Boolean.TRUE.equals(dto.getUpdateCategories())) {
            if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
                // 找出所有對應的標籤實體
                List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
                // 更新店家的標籤清單
                store.setCategories(categories);
            } else {
                // 若 updateCategories 為 true 且 categoryIds 為空或 null，則清空標籤
                store.setCategories(java.util.Collections.emptyList());
            }
        }
        return storeInfoRepository.save(store);
    }

    // 取得圖片上傳的路徑
    private Path getUploadPath() {
        Path rootPath = Paths.get("").toAbsolutePath();
        if (rootPath.toString().endsWith("back-end")) {
            return rootPath.getParent().resolve(uploadDir);
        } else {
            return rootPath.resolve(uploadDir);
        }
    }

    // 刪除圖片
    private void removeExistingImage(StoresInfo store) {
        if (store.getCoverImage() != null && !store.getCoverImage().isEmpty()) { //
            Path uploadPath = getUploadPath();
            Path oldFilePath = uploadPath.resolve(store.getCoverImage());
            try {
                // 嘗試刪除檔案
                boolean deleted = Files.deleteIfExists(oldFilePath);

                // 檔案一定要存在且成功刪除才算成功
                if (!deleted) {
                    throw new IOException("沒有圖片檔案，無法刪除");
                }

            } catch (IOException e) {
                // 拋出例外中斷執行
                throw new RuntimeException("無法刪除舊圖片，上傳終止: " + e.getMessage());
            }

            // 刪除成功(無報錯)執行
            store.setCoverImage(null);
        }
    }

    // 圖片上傳的執行
    private void handleImageUpload(StoresInfo store, MultipartFile file) {
        try {
            // 1. 確定儲存路徑
            Path uploadPath = getUploadPath();

            // 確保資料夾存在
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 2. 刪除原有圖片 (複用邏輯)
            removeExistingImage(store);

            // 3. 儲存新圖片
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 圖片檔案使用 storeId 和 timestamp 命名，確保唯一性
            String fileName = "store_" + store.getStoreId() + "_" + System.currentTimeMillis() + extension;
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            // 4. 更新 Entity 中的檔名
            store.setCoverImage(fileName);

        } catch (IOException e) {
            throw new RuntimeException("圖片儲存失敗: " + e.getMessage());
        }
    }

}
