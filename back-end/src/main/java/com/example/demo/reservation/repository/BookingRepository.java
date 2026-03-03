package com.example.demo.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.reservation.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

        @Query("SELECT COUNT(b) FROM Booking b WHERE b.store.storeId = :storeId " +
                        "AND b.bookingDate = :date AND b.reservedSeatType = :seatType " +
                        "AND b.status = true " +
                        "AND (CAST(:startTime AS time) < b.endTime AND CAST(:endTime AS time) > b.startTime)") // A 的開始時間 < B 的結束時間且 A 的結束時間 > B 的開始時間
        Integer countOverlappingBookings( // 跑完回傳一個int數字，代表有幾筆訂單跟目前的訂單時間重疊
                        @Param("storeId") Integer storeId,
                        @Param("date") LocalDate date,
                        @Param("seatType") Integer seatType,
                        @Param("startTime") LocalTime startTime,
                        @Param("endTime") LocalTime endTime);
}