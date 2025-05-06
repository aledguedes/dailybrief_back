package com.dailybrief.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dailybrief.dto.AnalyticsDTO;
import com.dailybrief.dto.LogResponseDTO;
import com.dailybrief.dto.PostRequestDTO;
import com.dailybrief.dto.PostResponseDTO;
import com.dailybrief.dto.UserRequestDTO;
import com.dailybrief.dto.UserResponseDTO;
import com.dailybrief.service.AnalyticsService;
import com.dailybrief.service.LogService;
import com.dailybrief.service.PostService;
import com.dailybrief.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private LogService logService;

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

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

    @Operation(summary = "Get all posts", description = "Fetches all posts in the system, including translations in PT, EN, ES")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of posts retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/posts")
    public List<PostResponseDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @Operation(summary = "Update a post", description = "Updates an existing post by its ID, including translations in PT, EN, ES")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Post updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid post data provided"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PutMapping("/posts/{id}")
    public PostResponseDTO updatePost(@Parameter(description = "ID of the post to update") @PathVariable Long id, @Valid @RequestBody PostRequestDTO postRequest) {
        return postService.updatePost(id, postRequest);
    }

    @Operation(summary = "Approve a post", description = "Changes the status of a post to APPROVED")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Post approved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PostMapping("/posts/{id}/approve")
    public PostResponseDTO approvePost(@Parameter(description = "ID of the post to approve") @PathVariable Long id) {
        return postService.approvePost(id);
    }

    @Operation(summary = "Reject a post", description = "Changes the status of a post to REJECTED")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Post rejected successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PostMapping("/posts/{id}/reject")
    public PostResponseDTO rejectPost(@Parameter(description = "ID of the post to reject") @PathVariable Long id) {
        return postService.rejectPost(id);
    }

    @Operation(summary = "Delete a post", description = "Deletes a post by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@Parameter(description = "ID of the post to delete") @PathVariable Long id) {
        postService.deletePost(id);
    }
}