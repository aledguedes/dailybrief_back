package com.dailybrief.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "tbl_material_process")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "task_id", unique = true, nullable = false)
    private String taskId;

    @Column(name = "theme", nullable = false)
    private String theme;

    @Column(name = "raw_material", columnDefinition = "TEXT")
    private String rawMaterial;

    @Column(name = "source_urls_json", columnDefinition = "TEXT")
    @JdbcTypeCode(SqlTypes.JSON)
    private String sourceUrlsJson;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "generated_content_json", columnDefinition = "TEXT")
    @JdbcTypeCode(SqlTypes.JSON)
    private String generatedContentJson;

    @Column(name = "suggested_image_prompt", columnDefinition = "TEXT")
    private String suggestedImagePrompt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}