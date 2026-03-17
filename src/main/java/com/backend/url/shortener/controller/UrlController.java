package com.backend.url.shortener.controller;

import com.backend.url.shortener.dto.AnalyticsResponse;
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
        return urlService.createShortUrl(urlRequest.getUrl(),urlRequest.getCustomCode(),urlRequest.getExpirationTime());
    }

    @GetMapping("/s/{shortCode}")
    public ResponseEntity<Void> getShortUrl(@PathVariable String shortCode){

        try {
            String originalUrl = urlService.getShortUrl(shortCode);


            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create(originalUrl))
                    .build();
        } catch (RuntimeException ex) {
            if (ex.getMessage().equals("URL has Expired")) {
                return ResponseEntity
                        .status(HttpStatus.GONE)
                        .build();
            }
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
   }
   @GetMapping("/analytics/{shortCode}")
    public ResponseEntity<AnalyticsResponse> getAnalytics(@PathVariable String shortCode){

        return  urlService.getAnalytics(shortCode);
   }
//   @GetMapping("/")
//    public String home(){
//        return "URL SHortener API Running";
//   }
}
