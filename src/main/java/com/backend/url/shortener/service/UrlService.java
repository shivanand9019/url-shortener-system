package com.backend.url.shortener.service;

import com.backend.url.shortener.dto.AnalyticsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {

    public String getShortUrl(String shortCode);


    public ResponseEntity<String> createShortUrl(String originalUrl);

    ResponseEntity<AnalyticsResponse> getAnalytics(String shortCode);
}
