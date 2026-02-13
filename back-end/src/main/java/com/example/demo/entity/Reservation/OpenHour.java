package com.example.demo.entity.Reservation;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "open_hours")
public class OpenHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer timeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoresInfo store;

    @Column(nullable = false)
    private Integer dayOfWeek; // 0-6

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;
}