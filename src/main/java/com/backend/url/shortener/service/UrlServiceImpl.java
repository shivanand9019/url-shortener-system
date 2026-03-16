package com.backend.url.shortener.service;

import com.backend.url.shortener.dto.AnalyticsResponse;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.backend.url.shortener.model.UrlMapping;
import com.backend.url.shortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class UrlServiceImpl implements UrlService{
    @Autowired
    private UrlRepository urlRepository;
    @Override
    public String getShortUrl(String shortCode) {
        UrlMapping mapping = urlRepository
                .findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("URL not found"));

        mapping.setClickCount(mapping.getClickCount()+1);
        urlRepository.save(mapping);

        return mapping.getOriginalUrl();


    }
    @Override
    public ResponseEntity<String> createShortUrl(String originalUrl,String customCode) {

   String shortCode;
   if(customCode!=null && !customCode.isBlank()) {
       Optional<UrlMapping> existing = urlRepository.findByShortCode(customCode);
       if (existing.isPresent()) {
           return ResponseEntity
                   .status(HttpStatus.BAD_REQUEST)
                   .body("Custom short code already exists");

       }

       shortCode = customCode;
   }
   else{
       shortCode = generateShortCode();
   }

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortCode(shortCode);
        urlMapping.setClickCount(0L);
        urlMapping.setCreatedAt(LocalDateTime.now());
        urlMapping.setExpirationTime(LocalDateTime.now().plusDays(7));
        urlRepository.save(urlMapping);

        String shortUrl =
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/s/")
                        .path(shortCode)
                        .toUriString();


        return new ResponseEntity<>(shortUrl,HttpStatus.CREATED);
    }

    private String generateShortCode(){
        String chars = "abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder shortCode  =new StringBuilder();
        for(int i=0;i<6;i++){
            shortCode.append(chars.charAt(random.nextInt(chars.length())));
        }
        return shortCode.toString();
    }
    @Override
    public ResponseEntity<AnalyticsResponse> getAnalytics(String shortCode) {

        UrlMapping urlMapping = urlRepository.findByShortCode(shortCode).orElseThrow( () -> new RuntimeException(String.valueOf(HttpStatus.NOT_FOUND)));

        AnalyticsResponse response = new AnalyticsResponse();
        response.setOriginalUrl(urlMapping.getOriginalUrl());
        response.setShortCode(urlMapping.getShortCode());
        response.setClickCount(urlMapping.getClickCount());
        response.setCreatedAt(urlMapping.getCreatedAt());




        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
