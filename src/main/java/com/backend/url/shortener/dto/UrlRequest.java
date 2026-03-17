package com.backend.url.shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlRequest {
    private String url;
    private String customCode;
    private LocalDateTime expirationTime;
}
