package com.example.demo.service;

import com.example.demo.dto.StoreRegistrationRequest;
import com.example.demo.entity.*;
import com.example.demo.entity.Reservation.StoresInfo;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.Reservation.StoreInfoRepository;
import com.example.demo.repository.StoreRegistrationRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreRegistrationService {

    private final StoreRegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final StoreInfoRepository storeInfoRepository;
    private final AdminRepository adminRepository;

    // 1. 建立新申請
    @Transactional
    public StoreRegistration createApplication(Long userId, StoreRegistrationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 允許已是商家的使用者提交申請 (作為修改資料用)

        // 檢查是否有 PENDING 或 RETURNED 的申請
        // 若有 RETURNED，應引導使用者使用 updateApplication，但這邊先做嚴格阻擋
        boolean hasPendingOrReturned = registrationRepository.existsByApplicantAndStatusIn(
                user, List.of(StoreRegistrationStatus.PENDING, StoreRegistrationStatus.RETURNED));

        if (hasPendingOrReturned) {
            throw new RuntimeException("User already has a pending or returned application");
        }

        StoreRegistration registration = StoreRegistration.builder()
                .applicant(user)
                .email(user.getEmail())
                .name(request.getOwnerName() != null ? request.getOwnerName() : user.getName())
                .phone(request.getStorePhone())
                .address(request.getStoreAddress())
                .storeName(request.getStoreName()) // Optional
                .status(StoreRegistrationStatus.PENDING)
                .build();

        return registrationRepository.save(registration);
    }

    // 2. 更新原單並重送 (針對 RETURNED 狀態)
    @Transactional
    public StoreRegistration updateApplication(Long userId, Integer registrationId, StoreRegistrationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        StoreRegistration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        if (!registration.getApplicant().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to this registration");
        }

        if (registration.getStatus() != StoreRegistrationStatus.RETURNED) {
            throw new RuntimeException("Only returned applications can be re-submitted");
        }

        // 更新資料
        if (request.getOwnerName() != null)
            registration.setName(request.getOwnerName());
        if (request.getStorePhone() != null)
            registration.setPhone(request.getStorePhone());
        if (request.getStoreAddress() != null)
            registration.setAddress(request.getStoreAddress());
        if (request.getStoreName() != null)
            registration.setStoreName(request.getStoreName());

        // 重置狀態與回覆
        registration.setStatus(StoreRegistrationStatus.PENDING);
        // registration.setReply(null); // 選擇性：保留舊的回覆直到下次審核，或者清空。這裡先保留。

        return registrationRepository.save(registration);
    }

    // 3. 查詢所有申請 (依狀態篩選，可分頁)
    public Page<StoreRegistration> findAll(StoreRegistrationStatus status, Pageable pageable) {
        if (status != null) {
            return registrationRepository.findByStatus(status, pageable);
        }
        return registrationRepository.findAll(pageable);
    }

    // 3.1 查詢特定使用者的申請
    public List<StoreRegistration> findByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return registrationRepository.findByApplicant(user);
    }

    // 4. 同意申請
    @Transactional
    public void approveApplication(Integer adminId, Integer registrationId, String opinion) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        StoreRegistration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        if (registration.getStatus() != StoreRegistrationStatus.PENDING) {
            throw new RuntimeException("Registration is not in PENDING status");
        }

        // update registration
        registration.setStatus(StoreRegistrationStatus.APPROVED);
        registration.setManager(admin);
        registration.setReply(opinion);
        registrationRepository.save(registration);

        // update user
        User user = registration.getApplicant();
        user.setIsStore(true);
        userRepository.save(user);

        // create or update store info
        StoresInfo storeInfo = storeInfoRepository.findByUser(user).orElse(new StoresInfo());
        storeInfo.setUser(user);
        storeInfo.setStoreName(
                registration.getStoreName() != null ? registration.getStoreName() : registration.getName() + "的店"); // Fallback
                                                                                                                    // name
        storeInfo.setStorePhone(registration.getPhone());
        storeInfo.setAddress(registration.getAddress());

        // Default values only for new StoresInfo
        if (storeInfo.getStoreId() == null) {
            storeInfo.setTimeSlot(30);
            storeInfo.setTimeLimit(90);
        }

        storeInfoRepository.save(storeInfo);
    }

    // 5. 退回申請
    @Transactional
    public void rejectApplication(Integer adminId, Integer registrationId, String opinion) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        StoreRegistration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        if (registration.getStatus() != StoreRegistrationStatus.PENDING) {
            throw new RuntimeException("Registration is not in PENDING status");
        }

        registration.setStatus(StoreRegistrationStatus.RETURNED);
        registration.setManager(admin);
        registration.setReply(opinion);

        registrationRepository.save(registration);
    }
}
