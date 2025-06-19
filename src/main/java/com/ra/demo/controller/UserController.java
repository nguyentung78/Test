package com.ra.demo.controller;

import com.ra.demo.model.dto.UserProfileUpdateDTO;
import com.ra.demo.model.dto.UserResponseDTO;
import com.ra.demo.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getProfile() {
        return ResponseEntity.ok(userService.getProfile());
    }

    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateProfile(
            @Valid @ModelAttribute UserProfileUpdateDTO userProfileUpdateDTO) {
        userService.updateProfile(userProfileUpdateDTO);
        return ResponseEntity.ok("Cập nhật hồ sơ thành công");
    }

    @DeleteMapping("/account")
    public ResponseEntity<String> deleteAccount() {
        userService.deleteAccount();
        return ResponseEntity.ok("Xóa tài khoản thành công");
    }
}