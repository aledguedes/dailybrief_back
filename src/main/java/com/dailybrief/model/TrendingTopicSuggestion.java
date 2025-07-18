package com.dailybrief.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tbl_trending_topic_suggestions")
public class TrendingTopicSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "topic_name", nullable = false, length = 255)
    private String topicName;

    @Column(name = "source", nullable = false, length = 50)
    private String source;

    @Column(name = "relevance_reason", nullable = false, length = 1000)
    private String relevanceReason;

    @Column(name = "url", length = 500)
    private String url;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PostStatus status = PostStatus.NEW;
}