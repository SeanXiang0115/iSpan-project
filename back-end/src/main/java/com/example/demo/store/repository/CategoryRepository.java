package com.example.demo.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.store.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
