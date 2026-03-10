package com.backend.url.shortener.service;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.backend.url.shortener.model.UrlMapping;
import com.backend.url.shortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlServiceImpl implements UrlService{
    @Autowired
    private UrlRepository urlRepository;
    @Override
    public String shortUrl(String shortCode) {
        Optional<UrlMapping> mapping = urlRepository.findByShortCode(shortCode);
        return mapping.map(UrlMapping::getOriginalUrl)
                .orElseThrow(()-> new RuntimeException("URL not found"));

    }
    @Override
    public ResponseEntity<String> createShortUrl(String originalUrl) {
        String shortCode = UUID.randomUUID().toString().substring(0,8);
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortCode(shortCode);
        urlMapping.setClickCount(0);
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
}
