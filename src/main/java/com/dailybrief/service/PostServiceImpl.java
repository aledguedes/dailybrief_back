package com.dailybrief.service;

import com.dailybrief.dto.PostRequestDTO;
import com.dailybrief.dto.PostResponseDTO;
import com.dailybrief.exception.PostNotFoundException;
import com.dailybrief.mapper.PostMapper;
import com.dailybrief.model.Post;
import com.dailybrief.model.PostStatus;
import com.dailybrief.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    private String estimateReadTime(String content) {
        int words = content.split("\\s+").length;
        int minutes = (int) Math.ceil(words / 200.0); // Média de 200 palavras por minuto
        return minutes + " min";
    }
}