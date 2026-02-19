package com.example.demo.controller.Reservation;

import com.example.demo.dto.Reservation.StoreCreateUpdateDto;
import com.example.demo.entity.Reservation.StoresInfo;
import com.example.demo.service.Reservation.StoreInfoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.ApiResponse;
import java.util.Optional;

@RestController
@RequestMapping("/api/owner/store")
@RequiredArgsConstructor
public class StoreInfoController {

    private final StoreInfoService storeInfoService;

    // 獲取當前登入店家的資訊
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<StoresInfo>> getMyStoreInfo() {
        Optional<StoresInfo> storeOpt = storeInfoService.getMyStoreInfo();

        if (storeOpt.isEmpty()) {
            return ResponseEntity.status(403).body(ApiResponse.error("您目前不具備店家身分"));
        }

        return ResponseEntity.ok(ApiResponse.success(storeOpt.get()));
    }

    // 更新當前店家的資訊 (支援圖片上傳，使用 multipart/form-data)
    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<StoresInfo>> updateMyStoreInfo(@Valid @ModelAttribute StoreCreateUpdateDto dto) {
        try {
            StoresInfo updatedStore = storeInfoService.updateMyStoreInfo(dto);
            return ResponseEntity.ok(ApiResponse.success("店家資訊更新成功", updatedStore));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
