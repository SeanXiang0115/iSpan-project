package com.example.demo.controller.Reservation;

import com.example.demo.dto.Reservation.StoreCreateUpdateDto;
import com.example.demo.entity.Reservation.StoresInfo;
import com.example.demo.service.Reservation.StoreInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner/store")
@RequiredArgsConstructor
public class StoreInfoController {

    private final StoreInfoService storeInfoService;

    /**
     * 獲取當前登入店家的資訊
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMyStoreInfo() {
        return storeInfoService.getMyStoreInfo()
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(403).body("您目前不具備店家身分"));
    }

    /**
     * 更新當前店家的名稱
     */
    @PutMapping("/me")
    public ResponseEntity<?> updateMyStoreName(@RequestBody StoreCreateUpdateDto dto) {
        try {
            StoresInfo updatedStore = storeInfoService.updateMyStoreName(dto);
            return ResponseEntity.ok(updatedStore);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
