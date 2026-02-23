package com.example.demo.dto.Reservation;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;


// 用於查詢店家資訊的 DTO (前台)
@Data
public class StoreDetailDto {
    private String storeName;

    private String description;

    private String address;

    private String storePhone;

    private Integer timeSlot;

    private Integer timeLimit;

    private MultipartFile imageFile;

    // 前台標籤只要名稱即可
    private List<Integer> categoryName;
}
