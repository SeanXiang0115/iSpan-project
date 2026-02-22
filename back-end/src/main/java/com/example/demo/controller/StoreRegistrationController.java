package com.example.demo.controller;

import com.example.demo.dto.StoreRegistrationRequest;
import com.example.demo.entity.StoreRegistration;
import com.example.demo.entity.StoreRegistrationStatus;
import com.example.demo.entity.User;
import com.example.demo.service.StoreRegistrationService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/store-registrations")
@RequiredArgsConstructor
public class StoreRegistrationController {

    private final StoreRegistrationService registrationService;
    private final UserService userService;
    private final com.example.demo.service.AdminService adminService;

    // 1. 提交新申請
    @PostMapping
    public ResponseEntity<?> submitApplication(@RequestBody StoreRegistrationRequest request) {
        // TODO: Get current user ID from security context/session
        // For now, assuming a way to get user. In real app, use
        // @AuthenticationPrincipal or SecurityContextHolder
        // Here we mock obtaining user ID logic or expect it from request (not secure)
        // or session.
        // Let's assume we retrieve the user from the session/token in a real scenario.
        // For this implementation, I will rely on the UserService to get 'currentUser'
        // if implemented,
        // OR temporarily passing userId in header for testing if security not fully set
        // up.
        // **IMPORTANT:** Adjust this to your actual authentication mechanism.

        // Pseudo-code for getting current user:
        // User currentUser = userService.getCurrentUser();
        // Long userId = currentUser.getId();

        // 由於不確定您的 Spring Security 設定，這裡先假設您有方法取得當前使用者。
        // 若您的 Controller 有統一的 BaseController 或 SecurityUtil，請替換此處。
        // 暫時透過 userService.getCurrentUser() (假設有) 或拋出示範錯誤。

        try {
            User currentUser = userService.getCurrentUserEntity(); // 您需要確保 UserService 有此方法
            StoreRegistration registration = registrationService.createApplication(currentUser.getId(), request);
            return ResponseEntity.ok(registration);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 2. 修改並重新提交 (針對被退回案件)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication(@PathVariable Integer id,
            @RequestBody StoreRegistrationRequest request) {
        try {
            User currentUser = userService.getCurrentUserEntity();
            StoreRegistration registration = registrationService.updateApplication(currentUser.getId(), id, request);
            return ResponseEntity.ok(registration);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 2.1 使用者修改申請 (針對 PENDING 狀態)
    @PutMapping("/{id}/pending-update")
    public ResponseEntity<?> updatePendingApplication(@PathVariable Integer id,
            @RequestBody StoreRegistrationRequest request) {
        try {
            User currentUser = userService.getCurrentUserEntity();
            StoreRegistration registration = registrationService.updatePendingApplication(currentUser.getId(), id,
                    request);
            return ResponseEntity.ok(registration);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 3. 管理員查詢列表
    @GetMapping
    public ResponseEntity<Page<StoreRegistration>> getAllApplications(
            @RequestParam(required = false) StoreRegistrationStatus status,
            @RequestParam(required = false) Boolean isUpdate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // TODO: Add Validations that current user is Admin

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<StoreRegistration> result = registrationService.findAll(status, isUpdate, pageRequest);
        return ResponseEntity.ok(result);
    }

    // 4. 使用者查詢自己的申請 (用於 UserInfoView)
    @GetMapping("/my")
    public ResponseEntity<List<StoreRegistration>> getMyApplications() {
        try {
            User currentUser = userService.getCurrentUserEntity();
            List<StoreRegistration> result = registrationService.findByUser(currentUser.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 5. 管理員審核 (同意/退回)
    @PutMapping("/{id}/review")
    public ResponseEntity<?> reviewApplication(
            @PathVariable Integer id,
            @RequestBody Map<String, String> payload) {
        // Payload expected: { "action": "approve"|"reject", "opinion": "..." }

        try {
            // Get current Admin ID from AdminService
            Integer adminId = adminService.getCurrentAdmin().getId();

            String action = payload.get("action");
            String opinion = payload.get("opinion");
            String lastUpdatedAt = payload.get("lastUpdatedAt"); // Dirty Write Prevention

            if ("approve".equalsIgnoreCase(action)) {
                registrationService.approveApplication(adminId, id, opinion, lastUpdatedAt);
            } else if ("reject".equalsIgnoreCase(action) || "return".equalsIgnoreCase(action)) {
                registrationService.rejectApplication(adminId, id, opinion, lastUpdatedAt);
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid action"));
            }

            return ResponseEntity.ok(Map.of("message", "Review completed"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
