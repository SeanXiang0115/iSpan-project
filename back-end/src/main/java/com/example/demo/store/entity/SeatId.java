package com.example.demo.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

/**
 * 複合主鍵類別：必須實作 Serializable介面，並且包含所有作為主鍵的欄位
 * 這裡的 SeatId 包含 storeId 和 seatType 兩個欄位，分別對應到 Store 的 ID 和座位類型
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatId implements Serializable {

    private Integer storeId;

    private Integer seatType;
}