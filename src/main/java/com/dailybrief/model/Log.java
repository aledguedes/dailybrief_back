package com.dailybrief.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Data
@Table(name = "tbl_log")
public class Log {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;
}
