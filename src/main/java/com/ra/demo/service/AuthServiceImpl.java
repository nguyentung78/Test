package com.ra.demo.service;

import com.ra.demo.model.dto.UserLoginRequestDTO;
import com.ra.demo.model.dto.UserLoginResponse;
import com.ra.demo.model.dto.UserRegisterRequestDTO;
import com.ra.demo.model.entity.Role;
import com.ra.demo.model.entity.Users;
import com.ra.demo.repository.RoleRepository;
import com.ra.demo.repository.UserRepository;
import com.ra.demo.security.UserPrinciple;
import com.ra.demo.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UploadService uploadService;

    @Override
    public UserLoginResponse login(UserLoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        String jwt = jwtProvider.generateToken(userPrinciple);

        return UserLoginResponse.builder()
                .username(userPrinciple.getUsername())
                .accessToken(jwt)
                .typeToken("Bearer")
                .roles(userPrinciple.getAuthorities().stream()
                        .map(GrantedAuthority -> GrantedAuthority.getAuthority().replace("ROLE_", ""))
                        .collect(Collectors.toList()))
                .avatar(userPrinciple.getUser().getAvatar())
                .build();
    }

    @Override
    @Transactional
    public void register(UserRegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Tên người dùng đã tồn tại");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        String avatarUrl = null;
        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            avatarUrl = uploadService.uploadFile(request.getAvatar());
        }

        Role userRole = roleRepository.findByRoleName(Role.RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Vai trò USER không tồn tại"));

        Users user = Users.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullname(request.getFullname())
                .phone(request.getPhone())
                .avatar(avatarUrl)
                .status(true)
                .isDeleted(false)
                .roles(new HashSet<>())
                .build();

        user.getRoles().add(userRole);
        userRepository.save(user);
    }
}