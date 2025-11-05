package com.dailybrief.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "tbl_users") 
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;
}
