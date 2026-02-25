package com.example.demo.repository.Reservation;

import com.example.demo.entity.Reservation.StoresInfo;
import com.example.demo.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreInfoRepository extends JpaRepository<StoresInfo, Integer> {
    Optional<StoresInfo> findByUser(User user);
}
