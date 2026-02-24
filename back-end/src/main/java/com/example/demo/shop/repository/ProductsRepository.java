package com.example.demo.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.shop.Products;

public interface ProductsRepository extends JpaRepository<Products, Integer> {
    
}