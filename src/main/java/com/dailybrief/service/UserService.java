package com.dailybrief.service;

import com.dailybrief.dto.UserRequestDTO;
import com.dailybrief.dto.UserResponseDTO;
import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequest);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();
}