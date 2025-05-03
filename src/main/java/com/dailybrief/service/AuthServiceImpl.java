package com.dailybrief.service;

import com.dailybrief.dto.LoginRequestDTO;
import com.dailybrief.dto.LoginResponseDTO;
import com.dailybrief.model.User;
import com.dailybrief.repository.UserRepository;
import com.dailybrief.config.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email());
        if (user != null && passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            String token = jwtTokenProvider.generateToken(user.getEmail());
            return new LoginResponseDTO(token);
        }
        throw new RuntimeException("Invalid credentials");
    }
}