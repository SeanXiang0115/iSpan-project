package com.example.demo.shop.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Entity
@Table(name = "order_logs")
@Data
public class OrderLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "log_level")
    private String logLevel; // INFO, ERROR, WARN

    @Column(name = "action")
    private String action;   // 例如: ECPAY_CALLBACK, ORDER_CREATE

    // 在 MSSQL 中，使用 NVARCHAR(MAX) 存放大數據
    @Column(name = "message", columnDefinition = "NVARCHAR(MAX)")
    private String message;

    // 這是你的面試亮點：紀錄原始 Payload
    @Column(name = "payload", columnDefinition = "NVARCHAR(MAX)")
    private String payload;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
}