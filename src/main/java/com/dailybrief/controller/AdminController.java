package com.dailybrief.controller;

import com.dailybrief.dto.AnalyticsDTO;
import com.dailybrief.dto.LogResponseDTO;
import com.dailybrief.dto.UserRequestDTO;
import com.dailybrief.dto.UserResponseDTO;
import com.dailybrief.service.AnalyticsService;
import com.dailybrief.service.LogService;
import com.dailybrief.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private LogService logService;

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all logs", description = "Fetches all audit logs in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of logs retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/logs")
    public List<LogResponseDTO> getLogs() {
        return logService.getLogs();
    }

    @Operation(summary = "Get analytics", description = "Fetches analytics data (e.g., pageviews, affiliate clicks)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Analytics data retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/analytics")
    public AnalyticsDTO getAnalytics() {
        return analyticsService.getAnalytics();
    }

    @Operation(summary = "Create a new user", description = "Creates a new user and returns the created user's details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequest) {
        UserResponseDTO response = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all users", description = "Fetches all users in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/users")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get a user by ID", description = "Fetches a user by its unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/users/{id}")
    public UserResponseDTO getUserById(@Parameter(description = "ID of the user to retrieve") @PathVariable Long id) {
        return userService.getUserById(id);
    }
}