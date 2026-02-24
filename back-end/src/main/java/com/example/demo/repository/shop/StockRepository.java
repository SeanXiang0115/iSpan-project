package com.example.demo.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.shop.Stock;

public interface  StockRepository extends JpaRepository<Stock, Integer> {
    
}
