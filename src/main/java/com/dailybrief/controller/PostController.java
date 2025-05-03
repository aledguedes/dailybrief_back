package com.dailybrief.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dailybrief.dto.PostRequestDTO;
import com.dailybrief.dto.PostResponseDTO;
import com.dailybrief.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostService postService;

    @Operation(summary = "Create a new post", description = "Creates a new blog post with translations in PT, EN, ES and returns the created post's details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid post data provided")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseDTO createPost(@RequestBody PostRequestDTO postDto) {
        return postService.createPost(postDto);
    }

    @Operation(summary = "Get all posts", description = "Fetches all posts available in the system, including translations in PT, EN, ES")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of all posts retrieved successfully")
    })
    @GetMapping
    public List<PostResponseDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @Operation(summary = "Get a post by ID", description = "Fetches a post by its unique ID, including translations in PT, EN, ES")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{id}")
    public PostResponseDTO getPostById(@Parameter(description = "ID of the post to retrieve") @PathVariable Long id) {
        return postService.getPostById(id);
    }

    @Operation(summary = "Health check", description = "Verifies the API status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "API is running")
    })
    @GetMapping("/health")
    public String healthCheck() {
        return "API is running!";
    }
}