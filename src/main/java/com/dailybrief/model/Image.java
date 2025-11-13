package com.dailybrief.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Data
@Table(name = "tbl_images")
public class Image {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "public_id", nullable = false, unique = true)
    private String publicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
