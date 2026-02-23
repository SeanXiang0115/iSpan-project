package com.example.demo.repository.Reservation;

import com.example.demo.entity.Reservation.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
