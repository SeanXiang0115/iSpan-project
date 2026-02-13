package com.example.demo.repository.Reservation;

import com.example.demo.entity.Reservation.StoresInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreInfoRepository extends JpaRepository<StoresInfo, Integer> {
    Optional<StoresInfo> findByUser_Id(Long userId);
}
