package com.example.demo.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.store.entity.Seat;
import com.example.demo.store.entity.SeatId;

public interface SeatRepository extends JpaRepository<Seat, SeatId> {

    // 邏輯：Find By [id屬性] 裡的 [storeId屬性]
    // 去 Seat 實體裡找名為 id 的屬性(型別為 SeatId)，再進去 id 裡面找 storeId 屬性
    // 根據 storeId 找出該店所有的座位設定（用於顯示在編輯頁面）
    List<Seat> findByIdStoreId(Integer storeId);

    // 根據 storeId 刪除所有座位設定（用於更新設定時，「先刪後增」的覆蓋邏輯）
    void deleteById_StoreId(Integer storeId);
}