package com.dailybrief.service.impl;

import com.dailybrief.dto.LoginRequestDTO;
import com.dailybrief.dto.LoginResponseDTO;
import com.dailybrief.model.User;
import com.dailybrief.repository.UserRepository;
import com.dailybrief.service.AuthService;
import com.dailybrief.config.JwtTokenProvider;

import java.util.Optional;

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
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.email());
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
                String token = jwtTokenProvider.generateToken(user.getEmail());
                return new LoginResponseDTO(token);
            }
        }
        
        throw new RuntimeException("Invalid credentials");
    }
}