package com.dailybrief.model;

import lombok.Data;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@Table(name = "tbl_post")
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "image")
  private String image;

  @Column(name = "author")
  private String author;

  @Column(name = "category")
  private String category;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private PostStatus status;

  @Column(name = "published_at")
  private Timestamp publishedAt;

  @Column(name = "read_time")
  private String readTime;

  @ElementCollection
  @CollectionTable(name = "tbl_post_title", joinColumns = @JoinColumn(name = "post_id"))
  @MapKeyColumn(name = "lang")
  @Column(name = "title", length = 500)
  private Map<String, String> title = new HashMap<>();

  @ElementCollection
  @CollectionTable(name = "tbl_post_content", joinColumns = @JoinColumn(name = "post_id"))
  @MapKeyColumn(name = "lang")
  @Column(name = "content", columnDefinition = "TEXT")
  private Map<String, String> content = new HashMap<>();

  @ElementCollection
  @CollectionTable(name = "tbl_post_excerpt", joinColumns = @JoinColumn(name = "post_id"))
  @MapKeyColumn(name = "lang")
  @Column(name = "excerpt", columnDefinition = "TEXT")
  private Map<String, String> excerpt = new HashMap<>();

  @ElementCollection
  @CollectionTable(name = "tbl_post_meta_description", joinColumns = @JoinColumn(name = "post_id"))
  @MapKeyColumn(name = "lang")
  @Column(name = "meta_description", length = 500)
  private Map<String, String> metaDescription = new HashMap<>();

  @ElementCollection
  @CollectionTable(name = "tbl_post_affiliate_link", joinColumns = @JoinColumn(name = "post_id"))
  @MapKeyColumn(name = "lang")
  @Column(name = "affiliate_link")
  private Map<String, String> affiliateLinks = new HashMap<>();

  @ElementCollection
  @CollectionTable(name = "tbl_post_tags", joinColumns = @JoinColumn(name = "post_id"))
  @Column(name = "tags")
  private List<String> tags = new ArrayList<>();
}
