package com.dailybrief.repository;

import com.dailybrief.model.Post;
import com.dailybrief.model.PostStatus;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    long count();

    long countByStatus(PostStatus status);

    Page<Post> findAllByOrderByPublishedAtDesc(Pageable pageable);

    @Query("SELECT p FROM Post p")
    @EntityGraph(attributePaths = { "title", "excerpt" })
    Page<Post> findAllPostsWithTitlesAndExcerpts(Pageable pageable);

    @Query("SELECT p FROM Post p")
    @EntityGraph(attributePaths = { "title", "excerpt", "tags" })
    Page<Post> findAllPostsWithAllDetails(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.status = :status")
    @EntityGraph(attributePaths = { "title", "excerpt", "tags" })
    Page<Post> findByStatus(PostStatus status, Pageable pageable);
    
    Optional<Post> findByIdAndStatus(Long id, PostStatus status);
}