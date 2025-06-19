package com.ra.demo.service;

import com.ra.demo.model.dto.UserProfileUpdateDTO;
import com.ra.demo.model.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO getProfile();
    void updateProfile(UserProfileUpdateDTO request);
    void deleteAccount();
}