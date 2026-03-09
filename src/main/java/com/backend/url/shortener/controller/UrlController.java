package com.backend.url.shortener.controller;

import com.backend.url.shortener.dto.UrlRequest;
import com.backend.url.shortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlController {
    @Autowired
    private UrlService urlService;

    @PostMapping("/api/shorten")
    public ResponseEntity<String> createShortUrl(@RequestBody UrlRequest urlRequest){
        return urlService.createShortUrl(urlRequest.getUrl());
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> shortUrl(@PathVariable String shortCode){
        String originalUrl = urlService.shortUrl(shortCode);


        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();

   }
}
