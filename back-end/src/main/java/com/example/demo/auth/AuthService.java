package com.example.demo.auth;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.common.exception.UserAlreadyExistsException;
import com.example.demo.common.security.JwtTokenProvider;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtTokenProvider tokenProvider;
        private final AuthenticationManager authenticationManager;
        private final LoginAttemptService loginAttemptService;

        @Value("${jwt.access-token-expiration-ms}")
        private long accessTokenExpirationMs;

        @Transactional
        public AuthResponse register(RegisterRequest request) {
                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new UserAlreadyExistsException("Email already registered: " + request.getEmail());
                }

                // 如果沒有提供 name，使用 email 的 @ 前綴作為預設名稱
                String displayName = request.getName();
                if (displayName == null || displayName.isBlank()) {
                        displayName = request.getEmail().split("@")[0];
                }

                User user = User.builder()
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .name(displayName)
                                .isStore(false)
                                .enabled(true)
                                .build();

                user = userRepository.save(user);

                String accessToken = tokenProvider.generateAccessToken(user.getEmail(),
                                Boolean.TRUE.equals(user.getIsStore()) ? "STORE" : "USER");
                String refreshToken = tokenProvider.generateRefreshToken(user.getEmail());

                return AuthResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .tokenType("Bearer")
                                .expiresIn(accessTokenExpirationMs / 1000) // in seconds
                                .user(mapToUserResponse(user))
                                .build();
        }

        public AuthResponse login(LoginRequest request) {
                // 使用 identifier（可以是 email 或 username）進行登入
                String identifier = request.getIdentifier();

                // 檢查是否已被防爆破機制鎖定
                if (loginAttemptService.isBlocked(identifier)) {
                        throw new RuntimeException("登入失敗3次，請15分鐘後再試，或點選忘記密碼重設");
                }

                // 第一步：驗證用戶名和密碼
                Authentication authentication;
                try {
                        authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        identifier,
                                                        request.getPassword()));
                } catch (org.springframework.security.core.AuthenticationException e) {
                        // 密碼錯誤，紀錄失敗次數
                        loginAttemptService.loginFailed(identifier);
                        throw new RuntimeException("登入失敗，請重新嘗試");
                }

                User user = userRepository.findByEmail(identifier)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // 第二步：檢查是否啟用 2FA
                if (Boolean.TRUE.equals(user.getTwoFactorEnabled())) {
                        // 如果啟用了 2FA，必須提供驗證碼
                        if (request.getTwoFactorCode() == null || request.getTwoFactorCode().isEmpty()) {
                                throw new RuntimeException("2FA code is required");
                        }

                        // 驗證 2FA 碼
                        if (!verify2FACode(user.getTwoFactorSecret(), request.getTwoFactorCode())) {
                                throw new RuntimeException("Invalid 2FA code");
                        }
                }

                // 第三步：生成 JWT tokens
                // login 使用 authentication，JwtTokenProvider 改寫後會自動從 authorities 抓取 role
                String accessToken = tokenProvider.generateAccessToken(authentication);
                String refreshToken = tokenProvider.generateRefreshToken(user.getEmail());

                // 登入成功，重置失敗計數
                loginAttemptService.loginSucceeded(identifier);

                return AuthResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .tokenType("Bearer")
                                .expiresIn(accessTokenExpirationMs / 1000) // in seconds
                                .user(mapToUserResponse(user))
                                .build();
        }

        private boolean verify2FACode(String secret, String code) {
                try {
                        com.warrenstrange.googleauth.GoogleAuthenticatorConfig config = new com.warrenstrange.googleauth.GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                                        .setWindowSize(10) // 容錯 10 個區間 = 5 分鐘
                                        .build();
                        com.warrenstrange.googleauth.GoogleAuthenticator gAuth = new com.warrenstrange.googleauth.GoogleAuthenticator(
                                        config);
                        int codeInt = Integer.parseInt(code);
                        return gAuth.authorize(secret, codeInt);
                } catch (NumberFormatException e) {
                        return false;
                }
        }

        public AuthResponse verifyOAuth2TwoFactor(com.example.demo.auth.dto.OAuth2TwoFactorRequest request) {
                if (!tokenProvider.validateToken(request.getPreAuthToken())) {
                        throw new RuntimeException("Invalid or expired 2FA token");
                }

                String email = tokenProvider.getEmailFromToken(request.getPreAuthToken());
                String roleClaim = tokenProvider.getRoleFromToken(request.getPreAuthToken());

                if (!"PRE_AUTH".equals(roleClaim)) {
                        throw new RuntimeException("Invalid token type");
                }

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                if (!verify2FACode(user.getTwoFactorSecret(), request.getCode())) {
                        throw new RuntimeException("Invalid 2FA code");
                }

                // Verify success -> issue real tokens
                String accessToken = tokenProvider.generateAccessToken(user.getEmail(),
                                Boolean.TRUE.equals(user.getIsStore()) ? "STORE" : "USER");
                String refreshToken = tokenProvider.generateRefreshToken(user.getEmail());

                return AuthResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .tokenType("Bearer")
                                .expiresIn(accessTokenExpirationMs / 1000)
                                .user(mapToUserResponse(user))
                                .build();
        }

        public AuthResponse refreshToken(String refreshToken) {
                if (!tokenProvider.validateToken(refreshToken)) {
                        throw new RuntimeException("Invalid refresh token");
                }

                String email = tokenProvider.getEmailFromToken(refreshToken);
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                String newAccessToken = tokenProvider.generateAccessToken(email,
                                Boolean.TRUE.equals(user.getIsStore()) ? "STORE" : "USER");
                String newRefreshToken = tokenProvider.generateRefreshToken(email);

                return AuthResponse.builder()
                                .accessToken(newAccessToken)
                                .refreshToken(newRefreshToken)
                                .tokenType("Bearer")
                                .expiresIn(accessTokenExpirationMs / 1000)
                                .user(mapToUserResponse(user))
                                .build();
        }

        private UserResponse mapToUserResponse(User user) {
                return UserResponse.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .name(user.getName())
                                .isStore(user.getIsStore())
                                .createdAt(user.getCreatedAt())
                                .build();
        }
}
