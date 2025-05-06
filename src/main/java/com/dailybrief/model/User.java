package com.dailybrief.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "tbl_user")  // Nome da tabela atualizado para "tbl_user"
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;
}
