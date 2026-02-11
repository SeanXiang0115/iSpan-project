package com.example.demo.controller;

import com.example.demo.dto.AdminLoginRequest;
import com.example.demo.dto.AdminLoginResponse;
import com.example.demo.dto.AdminRegistrationRequest;
import com.example.demo.dto.AdminResponse;
import com.example.demo.dto.ApiResponse;
import com.example.demo.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AdminLoginRequest request) {
        try {
            AdminLoginResponse response = adminService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Admin login successful", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminRegistrationRequest request) {
        try {
            AdminResponse response = adminService.registerAdmin(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Admin registered successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAdmins() {
        return ResponseEntity.ok(ApiResponse.success("Admins retrieved successfully", adminService.getAllAdmins()));
    }
}
