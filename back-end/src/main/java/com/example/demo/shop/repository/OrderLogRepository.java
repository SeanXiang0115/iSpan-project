package com.example.demo.shop.repository;

import com.example.demo.shop.entity.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLogRepository extends JpaRepository<OrderLog, Integer> {
}