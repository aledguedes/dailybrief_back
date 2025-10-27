package com.dailybrief.model;

import java.time.ZonedDateTime;
import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "materials")
public class Material {

    @Id
    @Column(name = "task_id", length = 255)
    private String taskId;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Column(name = "automation_request_id")
    private Integer automationRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Lob // Para mapear para TEXT
    @Column(name = "theme")
    private String theme;

    @Column(name = "content_type", length = 255)
    private String contentType;

    @Lob
    @Column(name = "raw_material_ids")
    private String rawMaterialIds; // Mapeado como String, considere um tipo mais estruturado (List<String>) se
                                   // necessário

    @Lob
    @Column(name = "generated_content")
    private String generatedContent;

    @Lob
    @Column(name = "suggested_image_prompt")
    private String suggestedImagePrompt;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @Lob
    @Column(name = "source_urls")
    private String sourceUrls; // Mapeado como String, considere um tipo mais estruturado (List<String>) se
                               // necessário

    // Getters and Setters (omitted for brevity)
    // Construtores (omitted for brevity)
}
