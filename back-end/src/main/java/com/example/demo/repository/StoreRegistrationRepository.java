package com.example.demo.repository;

import com.example.demo.entity.Admin;
import com.example.demo.entity.StoreRegistration;
import com.example.demo.entity.StoreRegistrationStatus;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRegistrationRepository extends JpaRepository<StoreRegistration, Integer> {

    // 依狀態查詢 (支援分頁)
    Page<StoreRegistration> findByStatus(StoreRegistrationStatus status, Pageable pageable);

    // 依申請人查詢 (通常一個 User 只會有少數幾筆，List 足夠)
    List<StoreRegistration> findByApplicant(User applicant);

    // 依申請人與狀態查詢 (用於檢查重複申請)
    boolean existsByApplicantAndStatus(User applicant, StoreRegistrationStatus status);

    // 依申請人與狀態列表查詢 (例如檢查是否有 PENDING 或 RETURNED 的申請)
    boolean existsByApplicantAndStatusIn(User applicant, List<StoreRegistrationStatus> statuses);

    // 依管理員查詢 (擴充性預留)
    Page<StoreRegistration> findByManager(Admin manager, Pageable pageable);
}
