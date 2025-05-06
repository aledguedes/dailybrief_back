package com.dailybrief.service;

import com.dailybrief.dto.PostRequestDTO;
import com.dailybrief.dto.PostResponseDTO;
import java.util.List;

public interface PostService {
    PostResponseDTO createPost(PostRequestDTO postRequest);
    List<PostResponseDTO> getAllPosts();
    PostResponseDTO getPostById(Long id);
    PostResponseDTO updatePost(Long id, PostRequestDTO postRequest);
    PostResponseDTO approvePost(Long id);
    PostResponseDTO rejectPost(Long id);
    void deletePost(Long id);
}