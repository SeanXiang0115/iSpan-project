package com.example.demo.service;

import com.example.demo.dto.AdminLoginRequest;
import com.example.demo.dto.AdminLoginResponse;
import com.example.demo.dto.AdminRegistrationRequest;
import com.example.demo.dto.AdminResponse;
import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;
import com.example.demo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Value("${jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;

    public AdminLoginResponse login(AdminLoginRequest request) {
        // 1. 根據 account 查詢 Admin
        Admin admin = adminRepository.findByAccount(request.getAccount())
                .orElseThrow(() -> new RuntimeException("Invalid account or password"));

        // 2. 檢查帳號是否被鎖定
        if (!Boolean.TRUE.equals(admin.getEnabled())) {
            throw new RuntimeException("Account is disabled");
        }

        // 3. 驗證密碼
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid account or password");
        }

        // 4. 生成 JWT tokens (包含 role="ADMIN" 和 position)
        String accessToken = tokenProvider.generateAccessToken(
                admin.getAccount(),
                "ADMIN",
                admin.getPosition());
        String refreshToken = tokenProvider.generateRefreshToken(admin.getAccount());

        // 5. 在 console 顯示登入資訊（測試用）
        System.out.println("========== ADMIN LOGIN SUCCESS ==========");
        System.out.println("Account: " + admin.getAccount());
        System.out.println("Name: " + admin.getName());
        System.out.println("Position: " + admin.getPosition());
        System.out.println("Access Token: " + accessToken);
        System.out.println("Refresh Token: " + refreshToken);
        System.out.println("=========================================");

        // 6. 返回包含 JWT 和 admin 資訊的 response
        return AdminLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenExpirationMs / 1000) // in seconds
                .admin(mapToAdminResponse(admin))
                .build();
    }

    public AdminResponse registerAdmin(AdminRegistrationRequest request) {
        // 1. 檢查帳號是否已存在
        if (adminRepository.existsByAccount(request.getAccount())) {
            throw new RuntimeException("Account already exists: " + request.getAccount());
        }

        // 2. 建立 Admin 物件，密碼預設為帳號（BCrypt 加密）
        Admin admin = Admin.builder()
                .account(request.getAccount())
                .name(request.getName())
                .position(request.getPosition())
                .password(passwordEncoder.encode(request.getAccount())) // 密碼 = 帳號（加密）
                .enabled(true) // 預設啟用
                .build();

        // 3. 儲存至資料庫
        admin = adminRepository.save(admin);

        // 4. 在 console 顯示註冊資訊（測試用）
        System.out.println("========== ADMIN REGISTRATION SUCCESS ==========");
        System.out.println("Account: " + admin.getAccount());
        System.out.println("Name: " + admin.getName());
        System.out.println("Position: " + admin.getPosition());
        System.out.println("Default Password: " + request.getAccount() + " (encrypted)");
        System.out.println("===============================================");

        // 5. 回傳 AdminResponse
        return mapToAdminResponse(admin);
    }

    public java.util.List<AdminResponse> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(this::mapToAdminResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    private AdminResponse mapToAdminResponse(Admin admin) {
        return AdminResponse.builder()
                .id(admin.getId())
                .account(admin.getAccount())
                .name(admin.getName())
                .position(admin.getPosition())
                .enabled(admin.getEnabled())
                .build();
    }
}
