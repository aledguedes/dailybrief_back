package com.dailybrief.service;

import com.dailybrief.dto.HomepagePostResponseDTO;
import com.dailybrief.dto.LocalizedPostResponseDTO;
import com.dailybrief.dto.PostRequestDTO;
import com.dailybrief.dto.PostResponseDTO;

import com.dailybrief.dto.LocalizedPostResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostResponseDTO createPost(PostRequestDTO postRequest);

    Page<PostResponseDTO> getAllPosts(Pageable pageable);

    Page<PostResponseDTO> getAllPostsLocalized(Pageable pageable);

    PostResponseDTO getPostById(String id);

    PostResponseDTO updatePost(String id, PostRequestDTO postRequest);

    PostResponseDTO approvePost(String id);

    PostResponseDTO rejectPost(String id);

    void deletePost(String id);

    // Mono<Post> saveGeneratedPost(FinalPostSubmissionRequestDTO requestDTO);
}