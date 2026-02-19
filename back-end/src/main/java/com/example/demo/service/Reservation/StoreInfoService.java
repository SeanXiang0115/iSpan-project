package com.example.demo.service.Reservation;

import com.example.demo.dto.Reservation.StoreCreateUpdateDto;
import com.example.demo.entity.Reservation.StoresInfo;
import com.example.demo.entity.User;
import com.example.demo.repository.Reservation.StoreInfoRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreInfoService {

    private final StoreInfoRepository storeInfoRepository;
    private final UserRepository userRepository;

    // 圖片儲存路徑（相對於專案根目錄，或是啟動目錄）
    private static final String UPLOAD_DIR = "front-end/public/pictures/StoreProfile";

    // 取得目前登入使用者的 User 實體
    private User getCurrentUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("找不到該登入使用者"));
    }

    // 取得目前登入使用者的店家資訊
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

        if (dto.getStorePhone() != null) {
            store.setStorePhone(dto.getStorePhone());
        }

        if (dto.getAddress() != null) {
            store.setAddress(dto.getAddress());
        }

        // 處理圖片：若有上傳新圖，則優先執行上傳(會覆蓋舊圖)；若無新圖但要求刪除，則執行刪除
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            handleImageUpload(store, dto.getImageFile());
        } else if (Boolean.TRUE.equals(dto.getRemoveImage())) {
            removeExistingImage(store);
        }

        return storeInfoRepository.save(store);
    }

    // 刪除圖片
    private void removeExistingImage(StoresInfo store) {
        if (store.getCoverImage() != null && !store.getCoverImage().isEmpty()) {
            Path uploadPath = getUploadPath();
            Path oldFilePath = uploadPath.resolve(store.getCoverImage());
            try {
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                System.err.println("刪除圖片檔案失敗: " + e.getMessage());
            }
            store.setCoverImage(null);
        }
    }

    // 上傳圖片(若有舊圖會呼叫刪除圖片方法並用新圖覆蓋)
    private Path getUploadPath() {
        Path rootPath = Paths.get("").toAbsolutePath();
        if (rootPath.toString().endsWith("back-end")) {
            return rootPath.getParent().resolve(UPLOAD_DIR);
        } else {
            return rootPath.resolve(UPLOAD_DIR);
        }
    }

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

            // 使用 storeId 和 timestamp 命名，確保唯一性
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
