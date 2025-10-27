package com.dailybrief.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "bg_class")
    private String bgClass;

    @Column(name = "text_class")
    private String textClass;
}