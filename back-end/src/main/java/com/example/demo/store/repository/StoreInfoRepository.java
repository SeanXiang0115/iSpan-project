package com.example.demo.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.store.entity.StoresInfo;

import java.util.Optional;

public interface StoreInfoRepository extends JpaRepository<StoresInfo, Integer> {

    @Query("SELECT s FROM StoresInfo s LEFT JOIN FETCH s.categories WHERE s.user.id = :userId")
    Optional<StoresInfo> findByUser_Id(@Param("userId") Long userId);
}
