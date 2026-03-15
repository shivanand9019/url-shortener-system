package com.backend.url.shortener.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String shortCode;
    private String originalUrl;
    private LocalDateTime createdAt;
    private Long clickCount;
    private LocalDateTime expirationTime;

}
