package com.dailybrief.service;

import com.dailybrief.dto.PostRequestDTO;
import com.dailybrief.dto.PostResponseDTO;
import com.dailybrief.dto.LocalizedPostResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PostResponseDTO createPost(PostRequestDTO postRequest);

    Page<PostResponseDTO> getAllPosts(Pageable pageable);

    Page<LocalizedPostResponseDTO> getAllPostsLocalized(Pageable pageable);

    PostResponseDTO getPostById(Long id);

    PostResponseDTO updatePost(Long id, PostRequestDTO postRequest);

    PostResponseDTO approvePost(Long id);

    PostResponseDTO rejectPost(Long id);

    void deletePost(Long id);
}