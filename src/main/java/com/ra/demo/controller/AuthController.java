package com.ra.demo.controller;

import com.ra.demo.model.dto.UserLoginRequestDTO;
import com.ra.demo.model.dto.UserLoginResponse;
import com.ra.demo.model.dto.UserRegisterRequestDTO;
import com.ra.demo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @ModelAttribute UserRegisterRequestDTO request) {
        authService.register(request);
        return ResponseEntity.ok("Đăng ký thành công");
    }
}