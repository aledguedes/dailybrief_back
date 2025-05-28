package com.dailybrief.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "automation_requests")
public class Automation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "output_format", nullable = false, length = 50)
    private String outputFormat;

    @Column(name = "theme", nullable = false, length = 255)
    private String theme;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}