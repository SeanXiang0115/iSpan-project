package com.example.demo.store.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "seats")
public class Seat {

    @EmbeddedId
    private SeatId id;

    @MapsId("storeId") // 映射複合主鍵中的 storeId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoresInfo store;

    @Column(name = "total_count", nullable = false)
    private Integer totalCount = 0;
}