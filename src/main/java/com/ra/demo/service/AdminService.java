package com.ra.demo.service;

import com.ra.demo.model.dto.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    Page<UserResponseDTO> getAllUsers(Pageable pageable);
}