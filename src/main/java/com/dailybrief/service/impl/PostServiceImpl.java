package com.dailybrief.service.impl;

import com.dailybrief.dto.HomepagePostResponseDTO;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

        private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

        private final PostRepository postRepository;
        private final PostMapper postMapper;
        private final StatusRepository statusRepository;

        public PostServiceImpl(PostRepository postRepository, PostMapper postMapper,
                        StatusRepository statusRepository) {
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
                                .orElseThrow(() -> new IllegalStateException(
                                                "Status with ID " + STATUS_PENDING_ID + " not found"));
                post.setStatus(defaultStatus);

                if (post.getReadTime() == null) {
                        post.setReadTime(estimateReadTime(postRequest.content().getOrDefault("pt", "")));
                }

                Post savedPost = postRepository.save(post);
                return postMapper.toResponse(savedPost);
        }

        @Override
        public Page<PostResponseDTO> getAllPosts(Pageable pageable) {
                return postRepository.findAllPostsWithTitlesAndExcerpts(pageable).map(postMapper::toResponse);
        }

        @Override
        public Page getAllPostsLocalized(Pageable pageable) {
                return postRepository.findAllPostsWithAllDetails(pageable)
                                .map(post -> postMapper.toLocalizedResponse(post));
        }

        @Override
        public PostResponseDTO getPostById(String id) {
                Post post = postRepository.findById(id)
                                .orElseThrow(() -> new PostNotFoundException("Post com id " + id + " não encontrado"));
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
                        post.setReadTime(estimateReadTime(postRequest.content().getOrDefault("pt", "")));
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
                                .orElseThrow(() -> new IllegalStateException(
                                                "Status with ID " + STATUS_REJECTED_ID + " not found"));

                post.setStatus(rejectedStatus);
                Post savedPost = postRepository.save(post);
                return postMapper.toResponse(savedPost);
        }

        @Override
        @Transactional
        public void deletePost(String id) {
                if (!postRepository.existsById(id)) {
                        throw new PostNotFoundException("Post com id " + id + " não encontrado");
                }
                postRepository.deleteById(id);
        }

        @Override
        public HomepagePostResponseDTO getHomepagePosts(int recentPostsLimit, String lang) {
                logger.info("Buscando posts da homepage com limite: {}. Idioma fornecido: {}, Idioma do LocaleContextHolder: {}",
                                recentPostsLimit, lang, LocaleContextHolder.getLocale().toLanguageTag());

                Pageable latestPageable = PageRequest.of(0, 1, Sort.by(
                                Sort.Order.desc("publishedAt").nullsLast(),
                                Sort.Order.desc("createdAt")));
                Page<Post> latestPostPage = postRepository.findByStatus(PostStatus.APPROVED, latestPageable);
                LocalizedPostResponseDTO latestPost = null;
                Long latestPostId = null;

                if (latestPostPage.hasContent()) {
                        Post post = latestPostPage.getContent().get(0);
                        logger.debug("Post mais recente encontrado - ID: {}, Status: {}, Título: {}, PublishedAt: {}, CreatedAt: {}",
                                        post.getId(), post.getStatus(), post.getTitle(), post.getPublishedAt(),
                                        post.getCreatedAt());
                        if (post.getTitle() != null && post.getTitle().keySet().stream()
                                        .anyMatch(key -> key.equalsIgnoreCase("pt"))) {
                                latestPost = postMapper.toLocalizedResponse(post, lang);
                                latestPostId = post.getId();
                                logger.info("Post ID {} selecionado como latestPost", post.getId());
                        } else {
                                logger.warn("Post ID {} ignorado: título não contém chave 'pt' (case-insensitive). Título: {}",
                                                post.getId(), post.getTitle());
                        }
                } else {
                        logger.info("Nenhum post aprovado encontrado para latestPost.");
                }

                final Long finalLatestPostId = latestPostId;
                Pageable recentPageable = PageRequest.of(0, recentPostsLimit, Sort.by(
                                Sort.Order.desc("publishedAt").nullsLast(),
                                Sort.Order.desc("createdAt")));
                Page<Post> recentPostsPage = postRepository.findByStatus(PostStatus.APPROVED, recentPageable);
                List<LocalizedPostResponseDTO> recentPosts = recentPostsPage.getContent().stream()
                                .peek(post -> logger.debug("Post recente encontrado - ID: {}, Status: {}, Título: {}",
                                                post.getId(), post.getStatus(), post.getTitle()))
                                .filter(post -> {
                                        boolean isValid = post.getTitle() != null &&
                                                        post.getTitle().keySet().stream()
                                                                        .anyMatch(key -> key.equalsIgnoreCase("pt"));
                                        if (!isValid) {
                                                logger.warn("Post ID {} ignorado: título não contém chave 'pt' (case-insensitive). Título: {}",
                                                                post.getId(), post.getTitle());
                                        }
                                        return isValid && (finalLatestPostId == null
                                                        || !post.getId().equals(finalLatestPostId));
                                })
                                .map(post -> postMapper.toLocalizedResponse(post, lang))
                                .limit(recentPostsLimit)
                                .toList();

                logger.info("Retornando latestPost: {}, recentPosts: {} posts",
                                latestPost != null ? "ID " + latestPost.id() : "null", recentPosts.size());

                return new HomepagePostResponseDTO(latestPost, recentPosts);
        }

        @Override
        public LocalizedPostResponseDTO getPublicPostById(Long id, String lang) {
                Post post = postRepository.findById(id)
                                .filter(p -> p.getStatus() == PostStatus.APPROVED)
                                .orElseThrow(() -> new PostNotFoundException(
                                                "Post aprovado com id " + id + " não encontrado"));
                return postMapper.toLocalizedResponse(post, lang);
        }

        private String estimateReadTime(String content) {
                int words = content.split("\\s+").length;
                int minutes = (int) Math.ceil(words / 200.0);
                return minutes + " min";
        }
}
