package com.example.demo.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.reservation.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

}