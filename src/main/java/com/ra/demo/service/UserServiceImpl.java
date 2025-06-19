package com.ra.demo.service;

import com.ra.demo.model.dto.UserProfileUpdateDTO;
import com.ra.demo.model.dto.UserResponseDTO;
import com.ra.demo.model.entity.Users;
import com.ra.demo.repository.UserRepository;
import com.ra.demo.security.UserPrinciple;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogoutService logoutService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UploadService uploadService;

    private Users getCurrentAuthenticatedUser() {
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserByUsername(userPrinciple.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
    }

    @Override
    public UserResponseDTO getProfile() {
        Users user = getCurrentAuthenticatedUser();
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullname(user.getFullname())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .status(user.isStatus())
                .roles(user.getRoles().stream()
                        .map(role -> role.getRoleName().name())
                        .collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public void updateProfile(UserProfileUpdateDTO request) {
        Users user = getCurrentAuthenticatedUser();
        boolean isUpdated = false;

        // Kiểm tra và cập nhật username
        if (request.getUsername() != null && !request.getUsername().trim().isEmpty()) {
            if (!request.getUsername().equals(user.getUsername())) {
                Users existingUser = userRepository.findUserByUsername(request.getUsername()).orElse(null);
                if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                    throw new IllegalArgumentException("Username đã tồn tại!");
                }
                user.setUsername(request.getUsername());
                isUpdated = true;
            }
        }

        // Kiểm tra và cập nhật email
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            if (!request.getEmail().equals(user.getEmail())) {
                Users existingEmailUser = userRepository.findByEmail(request.getEmail()).orElse(null);
                if (existingEmailUser != null && !existingEmailUser.getId().equals(user.getId())) {
                    throw new IllegalArgumentException("Email đã tồn tại!");
                }
                user.setEmail(request.getEmail());
                isUpdated = true;
            }
        }

        // Kiểm tra và cập nhật phone
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            if (!request.getPhone().equals(user.getPhone()) && userRepository.existsByPhone(request.getPhone())) {
                throw new IllegalArgumentException("Số điện thoại đã tồn tại!");
            }
            user.setPhone(request.getPhone());
            isUpdated = true;
        }

        // Kiểm tra và cập nhật fullname
        if (request.getFullname() != null && !request.getFullname().trim().isEmpty()
                && !request.getFullname().equals(user.getFullname())) {
            user.setFullname(request.getFullname());
            isUpdated = true;
        }

        // Kiểm tra và cập nhật avatar
        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            String imageUrl = uploadService.uploadFile(request.getAvatar());
            user.setAvatar(imageUrl);
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new IllegalArgumentException("Không có dữ liệu nào để cập nhật!");
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteAccount() {
        Users user = getCurrentAuthenticatedUser();
        if (user.isDeleted()) {
            throw new IllegalStateException("Tài khoản đã bị xóa");
        }
        user.setDeleted(true);
        user.setStatus(false);
        userRepository.save(user);

        String token = getTokenFromRequest();
        if (token != null) {
            logoutService.blacklistToken(token);
        }
    }

    private String getTokenFromRequest() {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}