package com.dailybrief.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Data
@Table(name = "tbl_log")  // Nome da tabela atualizado para "tbl_log"
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action")
    private String action; // Ex.: "Post #1 aprovado"
    
    @Column(name = "created_by")
    private String createdBy; // Ex.: "admin"
    
    @Column(name = "timestamp")
    private Instant timestamp;
}
