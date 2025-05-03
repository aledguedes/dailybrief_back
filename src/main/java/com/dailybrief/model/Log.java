package com.dailybrief.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Data
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String action; // Ex.: "Post #1 aprovado"
    private String user; // Ex.: "admin"
    private Instant timestamp;
}