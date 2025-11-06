package com.dailybrief.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.Instant;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@Table(name = "tbl_logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_id", nullable = false)
    private String reportId;

    @Column(name = "level", nullable = false)
    private String level;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "details", columnDefinition = "JSONB")
    private String details;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;
}