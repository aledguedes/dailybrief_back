package com.dailybrief.service.impl;

import com.dailybrief.dto.LocalizedPostResponseDTO;
import com.dailybrief.dto.PostRequestDTO;
import com.dailybrief.dto.PostResponseDTO;
import com.dailybrief.exception.PostNotFoundException;
import com.dailybrief.mapper.PostMapper;
import com.dailybrief.model.Post;
import com.dailybrief.model.Status;
import com.dailybrief.repository.PostRepository;
import com.dailybrief.repository.StatusRepository;
import com.dailybrief.service.PostService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final StatusRepository statusRepository;


    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper, StatusRepository statusRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.statusRepository = statusRepository;
    }

    @Override
    @Transactional
    public PostResponseDTO createPost(PostRequestDTO postRequest) {
    	Integer STATUS_PENDING_ID = 10;
        Post post = postMapper.toEntity(postRequest);

        Status defaultStatus = statusRepository.findById(STATUS_PENDING_ID)
                .orElseThrow(() -> new IllegalStateException("Status with ID " + STATUS_PENDING_ID + " not found"));
        post.setStatus(defaultStatus);

        if (post.getReadTime() == null) {
            post.setReadTime(estimateReadTime(postRequest.content().getOrDefault("pt", "")));
        }

        Post savedPost = postRepository.save(post);
        return postMapper.toResponse(savedPost);
    }

    @Override
    public Page<PostResponseDTO> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(postMapper::toResponse);
    }

    @Override
    public Page<LocalizedPostResponseDTO> getAllPostsLocalized(Pageable pageable) {
        return postRepository.findAll(pageable).map(postMapper::toLocalizedResponse);
    }

    @Override
    public PostResponseDTO getPostById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));
        return postMapper.toResponse(post);
    }

    @Override
    @Transactional
    public PostResponseDTO updatePost(String id, PostRequestDTO postRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));

        Post updatedPost = postMapper.toEntity(postRequest);
        updatedPost.setId(id);
        updatedPost.setStatus(post.getStatus()); // mantém o status existente

        if (updatedPost.getReadTime() == null) {
            updatedPost.setReadTime(estimateReadTime(postRequest.content().getOrDefault("pt", "")));
        }

        Post savedPost = postRepository.save(updatedPost);
        return postMapper.toResponse(savedPost);
    }

    @Override
    @Transactional
    public PostResponseDTO approvePost(String id) {
    	Integer STATUS_APPROVED_ID = 15;
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));

        Status approvedStatus = statusRepository.findById(STATUS_APPROVED_ID)
                .orElseThrow(() -> new IllegalStateException("Status with ID 15 not found"));

        post.setStatus(approvedStatus);
        Post savedPost = postRepository.save(post);
        return postMapper.toResponse(savedPost);
    }


    @Override
    @Transactional
    public PostResponseDTO rejectPost(String id) {
    	Integer STATUS_REJECTED_ID = 16;
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));

        Status rejectedStatus = statusRepository.findById(STATUS_REJECTED_ID)
                .orElseThrow(() -> new IllegalStateException("Status with ID " + STATUS_REJECTED_ID + " not found"));

        post.setStatus(rejectedStatus);
        Post savedPost = postRepository.save(post);
        return postMapper.toResponse(savedPost);
    }

    @Override
    @Transactional
    public void deletePost(String id) {
        if (!postRepository.existsById(id)) {
            throw new PostNotFoundException("Post with id " + id + " not found");
        }
        postRepository.deleteById(id);
    }

    private String estimateReadTime(String content) {
        int words = content.split("\\s+").length;
        int minutes = (int) Math.ceil(words / 200.0);
        return minutes + " min";
    }
}
