package com.dailybrief.service;

import com.dailybrief.dto.HomepagePostResponseDTO;
import com.dailybrief.dto.LocalizedPostResponseDTO;
import com.dailybrief.dto.PostRequestDTO;
import com.dailybrief.dto.PostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostResponseDTO createPost(PostRequestDTO postRequest);

    Page<PostResponseDTO> getAllPosts(Pageable pageable);

    Page<PostResponseDTO> getAllPostsLocalized(Pageable pageable);

    PostResponseDTO getPostById(Long id);

    PostResponseDTO updatePost(Long id, PostRequestDTO postRequest);

    PostResponseDTO approvePost(Long id);

    PostResponseDTO rejectPost(Long id);

    void deletePost(Long id);

    HomepagePostResponseDTO getHomepagePosts(int recentPostsLimit, String lang);
    
    LocalizedPostResponseDTO getPublicPostById(Long id, String lang);
}