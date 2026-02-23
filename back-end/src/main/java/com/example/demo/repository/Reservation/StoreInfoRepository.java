package com.example.demo.repository.Reservation;

import com.example.demo.entity.Reservation.StoresInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreInfoRepository extends JpaRepository<StoresInfo, Integer> {

    @Query("SELECT s FROM StoresInfo s LEFT JOIN FETCH s.categories WHERE s.user.id = :userId")
    Optional<StoresInfo> findByUser_Id(@Param("userId") Long userId);
}
