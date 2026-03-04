package com.example.demo.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.shop.entity.Stock;

public interface  StockRepository extends JpaRepository<Stock, Integer> {
    
}
