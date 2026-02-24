package com.example.demo.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.shop.Stock;

public interface  StockRepository extends JpaRepository<Stock, Integer> {
    
}
