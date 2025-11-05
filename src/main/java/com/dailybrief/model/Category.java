package com.dailybrief.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "tbl_categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "description", length = 255)
    private String description;
    
    @Column(name = "target_audience", length = 50)
    private String targetAudience; // Ex: "TÉCNICO", "LEIGO", "MISTO"
}