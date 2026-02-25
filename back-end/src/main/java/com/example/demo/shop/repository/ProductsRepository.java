package com.example.demo.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.shop.entity.Products;

public interface ProductsRepository extends JpaRepository<Products, Integer> {
    
}