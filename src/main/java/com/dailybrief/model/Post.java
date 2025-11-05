package com.dailybrief.model;

import lombok.Data;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID; // Adicionar o import para UUID

@Entity
@Data
@Table(name = "tbl_posts")
public class Post {

	@Id
	@Column(name = "id", length = 36)
	private String id;

	@Column(name = "image")
	private String image;

	@Column(name = "author")
	private String author;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

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

	@Column(name = "created_at", updatable = false)
	private Timestamp createdAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@PrePersist
	protected void onCreate() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
		createdAt = Timestamp.from(Instant.now());
		updatedAt = createdAt;
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = Timestamp.from(Instant.now());
	}
}