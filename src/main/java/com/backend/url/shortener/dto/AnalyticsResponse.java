package com.backend.url.shortener.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnalyticsResponse {

    private String originalUrl;
    private String shortCode;
    private Long clickCount;
    private LocalDateTime createdAt;

}