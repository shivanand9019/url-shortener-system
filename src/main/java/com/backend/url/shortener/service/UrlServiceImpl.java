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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlServiceImpl implements UrlService{
    @Autowired
    private UrlRepository urlRepository;
    @Override
    public String getShortUrl(String shortCode) {

        UrlMapping mapping = cache.get(shortCode);
        if(mapping!=null){
            if(mapping.getExpirationTime()!=null && LocalDateTime.now().isAfter(mapping.getExpirationTime())){
                throw new RuntimeException("URL has Expired");
            }
            mapping.setClickCount(mapping.getClickCount()+1);
            urlRepository.save(mapping);

            return mapping.getOriginalUrl();


        }else{
            UrlMapping url = urlRepository
                    .findByShortCode(shortCode)
                    .orElseThrow(() -> new RuntimeException("URL not found"));


            if(url.getExpirationTime()!=null && LocalDateTime.now().isAfter(url.getExpirationTime())){
                throw new RuntimeException("URL has Expired");
            }

            cache.put(shortCode,url);
            url.setClickCount(url.getClickCount()+1);
            urlRepository.save(url);

            return url.getOriginalUrl();
        }

    }
    @Override
    public ResponseEntity<String> createShortUrl(String originalUrl,String customCode,LocalDateTime expirationTime) {

        if(originalUrl==null || originalUrl.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("URL cannot be empty");
        }
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
       do {
           shortCode = generateShortCode();
       }while(urlRepository.findByShortCode(shortCode).isPresent());
   }





        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortCode(shortCode);
        urlMapping.setClickCount(0L);
        urlMapping.setCreatedAt(LocalDateTime.now());
        urlMapping.setExpirationTime(expirationTime);
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

        UrlMapping urlMapping = urlRepository.
                findByShortCode(shortCode).orElseThrow( () -> new RuntimeException("URL not Found"));

        AnalyticsResponse response = new AnalyticsResponse();
        response.setOriginalUrl(urlMapping.getOriginalUrl());
        response.setShortCode(urlMapping.getShortCode());
        response.setClickCount(urlMapping.getClickCount());
        response.setCreatedAt(urlMapping.getCreatedAt());




        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    private Map<String,UrlMapping> cache = new HashMap<>();

}
