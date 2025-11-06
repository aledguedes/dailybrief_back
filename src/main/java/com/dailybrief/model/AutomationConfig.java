package com.dailybrief.model;

import java.time.ZonedDateTime;
import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "tbl_automation_configs")
public class AutomationConfig {

    @Id
    @Column(name = "task_id", length = 255)
    private String taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Lob
    @Column(name = "search_factors", nullable = false)
    private String searchFactors; // Mapeado como String, considere JSONB com libs específicas se necessário

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;
}
