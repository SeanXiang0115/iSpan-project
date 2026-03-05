package com.example.demo.auth;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.ForgotPasswordRequest;
import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.OAuth2TwoFactorRequest;
import com.example.demo.auth.dto.RefreshTokenRequest;
import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.auth.dto.ResetPasswordRequest;
import com.example.demo.common.dto.ApiResponse;
import com.example.demo.user.UserService;
import com.example.demo.user.dto.UserResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return addTokensToCookies(ResponseEntity.status(HttpStatus.CREATED), response)
                .body(ApiResponse.success("User registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return addTokensToCookies(ResponseEntity.ok(), response)
                .body(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request.getRefreshToken());
        return addTokensToCookies(ResponseEntity.ok(), response)
                .body(ApiResponse.success("Token refreshed successfully", response));
    }

    @PostMapping("/oauth2/2fa-verify")
    public ResponseEntity<ApiResponse<AuthResponse>> oauth2Verify2FA(
            @Valid @RequestBody OAuth2TwoFactorRequest request) {
        AuthResponse response = authService.verifyOAuth2TwoFactor(request);
        return addTokensToCookies(ResponseEntity.ok(), response)
                .body(ApiResponse.success("OAuth2 Login successful", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        UserResponse response = userService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 忘記密碼 - 發送重設郵件
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordResetService.initiatePasswordReset(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("Password reset email sent", null));
    }

    /**
     * 重設密碼 - 使用 token
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        // 驗證密碼確認
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success("Password reset successfully", null));
    }

    /**
     * 登出 - 清除 Cookie
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        ResponseCookie jwtCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false) // in production use true
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResponse.success("Logout successful", null));
    }

    private ResponseEntity.BodyBuilder addTokensToCookies(ResponseEntity.BodyBuilder builder, AuthResponse response) {
        if (response.getAccessToken() != null) {
            ResponseCookie jwtCookie = ResponseCookie.from("accessToken", response.getAccessToken())
                    .httpOnly(true)
                    .secure(false) // in production use true
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .build();
            builder.header(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        }

        if (response.getRefreshToken() != null) {
            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", response.getRefreshToken())
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .build();
            builder.header(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        }

        // 為了安全，將 Token 從 Response Body 中移除
        response.setAccessToken(null);
        response.setRefreshToken(null);

        return builder;
    }
}
