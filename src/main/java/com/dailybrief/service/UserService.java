package com.dailybrief.service;

import com.dailybrief.dto.UserRequestDTO;
import com.dailybrief.dto.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequest);

    UserResponseDTO getUserById(Long id);

    Page<UserResponseDTO> getAllUsers(Pageable pageable);
}