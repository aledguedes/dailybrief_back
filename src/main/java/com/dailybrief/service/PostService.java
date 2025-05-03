package com.dailybrief.service;

import com.dailybrief.dto.PostRequestDTO;
import com.dailybrief.dto.PostResponseDTO;

import java.util.List;

public interface PostService {
    PostResponseDTO createPost(PostRequestDTO postRequest);
    List<PostResponseDTO> getAllPosts();
    PostResponseDTO getPostById(Long id);
}
