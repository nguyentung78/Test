package com.ra.demo.service;

import com.ra.demo.model.dto.UserLoginRequestDTO;
import com.ra.demo.model.dto.UserLoginResponse;
import com.ra.demo.model.dto.UserRegisterRequestDTO;

public interface AuthService {
    UserLoginResponse login(UserLoginRequestDTO request);
    void register(UserRegisterRequestDTO request);
}