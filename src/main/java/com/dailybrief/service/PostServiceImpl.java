package com.dailybrief.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dailybrief.dto.PostRequestDTO;
import com.dailybrief.dto.PostResponseDTO;
import com.dailybrief.exception.PostNotFoundException;
import com.dailybrief.mapper.PostMapper;
import com.dailybrief.model.Post;
import com.dailybrief.model.PostStatus;
import com.dailybrief.repository.PostRepository;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    @Transactional
    public PostResponseDTO createPost(PostRequestDTO postRequest) {
        Post post = postMapper.toEntity(postRequest);
        post.setStatus(PostStatus.PENDING); // Default
        if (post.getReadTime() == null) {
            post.setReadTime(estimateReadTime(postRequest.content().getOrDefault("pt", "")));
        }
        Post savedPost = postRepository.save(post);
        return postMapper.toResponse(savedPost);
    }

    @Override
    public List<PostResponseDTO> getAllPosts() {
        return postMapper.toResponseList(postRepository.findAll());
    }

    @Override
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));
        return postMapper.toResponse(post);
    }

    @Override
    @Transactional
    public PostResponseDTO updatePost(Long id, PostRequestDTO postRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));
        Post updatedPost = postMapper.toEntity(postRequest);
        updatedPost.setId(id);
        updatedPost.setStatus(post.getStatus()); // Preserva status
        if (updatedPost.getReadTime() == null) {
            updatedPost.setReadTime(estimateReadTime(postRequest.content().getOrDefault("pt", "")));
        }
        Post savedPost = postRepository.save(updatedPost);
        return postMapper.toResponse(savedPost);
    }

    @Override
    @Transactional
    public PostResponseDTO approvePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));
        post.setStatus(PostStatus.APPROVED);
        Post savedPost = postRepository.save(post);
        return postMapper.toResponse(savedPost);
    }

    @Override
    @Transactional
    public PostResponseDTO rejectPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));
        post.setStatus(PostStatus.APPROVED);
        Post savedPost = postRepository.save(post);
        return postMapper.toResponse(savedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new PostNotFoundException("Post with id " + id + " not found");
        }
        postRepository.deleteById(id);
    }

    private String estimateReadTime(String content) {
        int words = content.split("\\s+").length;
        int minutes = (int) Math.ceil(words / 200.0); // Média de 200 palavras por minuto
        return minutes + " min";
    }
}