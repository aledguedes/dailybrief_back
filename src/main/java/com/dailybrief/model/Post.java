package com.dailybrief.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Entity
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @MapKeyColumn(name = "lang")
    @Column(name = "title")
    private Map<String, String> title;

    @ElementCollection
    @MapKeyColumn(name = "lang")
    @Column(name = "excerpt")
    private Map<String, String> excerpt;

    @ElementCollection
    @MapKeyColumn(name = "lang")
    @Column(name = "content", columnDefinition = "TEXT")
    private Map<String, String> content;

    private String image;
    private String author;

    @ElementCollection
    private List<String> tags;

    private String category; // Ex.: "Technology"

    @ElementCollection
    @MapKeyColumn(name = "lang")
    @Column(name = "meta_description")
    private Map<String, String> metaDescription; // SEO

    @ElementCollection
    @MapKeyColumn(name = "lang")
    @Column(name = "affiliate_link")
    private Map<String, String> affiliateLinks; // Hotmart, ClickBank, Amazon

    @Enumerated(EnumType.STRING)
    private PostStatus status; // PENDING, APPROVED, REJECTED

    private Instant publishedAt;
    private String readTime;
}