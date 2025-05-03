package com.dailybrief.service;

import com.dailybrief.dto.LoginRequestDTO;
import com.dailybrief.dto.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO LoginRequest);
}
