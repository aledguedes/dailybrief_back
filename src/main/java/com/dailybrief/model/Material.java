package com.dailybrief.model;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.Data;
import jakarta.persistence.*;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;

@Entity
@Data
@Table(name = "tbl_materials")
public class Material {

    @Id
    @Column(name = "task_id", length = 255)
    private String taskId;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Column(name = "post_id", length = 255)
    private String postId;

    @Lob
    @Column(name = "theme")
    private String theme;

    @Column(name = "content_type", length = 255)
    private String contentType;

    @Type(JsonType.class)
    @Column(name = "raw_material_ids", columnDefinition = "jsonb")
    private List<String> rawMaterialIds;

    @Lob
    @Column(name = "suggested_image_prompt")
    private String suggestedImagePrompt;

    @Type(JsonType.class)
    @Column(name = "source_urls", columnDefinition = "jsonb")
    private List<String> sourceUrls;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
