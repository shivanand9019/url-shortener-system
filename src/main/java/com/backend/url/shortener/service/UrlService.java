package com.backend.url.shortener.service;

import com.backend.url.shortener.dto.AnalyticsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public interface UrlService {

    public String getShortUrl(String shortCode,String clientIp);


    public ResponseEntity<String> createShortUrl(String originalUrl, String customCode, LocalDateTime expirationTime);

    ResponseEntity<AnalyticsResponse> getAnalytics(String shortCode);
}
