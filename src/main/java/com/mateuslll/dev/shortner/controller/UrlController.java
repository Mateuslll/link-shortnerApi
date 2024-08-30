package com.mateuslll.dev.shortner.controller;

import com.mateuslll.dev.shortner.controller.dto.ShortenUrlRequest;
import com.mateuslll.dev.shortner.controller.dto.ShortenUrlResponse;
import com.mateuslll.dev.shortner.entities.UrlEntity;
import com.mateuslll.dev.shortner.repository.UrlRepository;
import org.apache.commons.lang3.RandomStringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.time.LocalDateTime;

public class UrlController {
    private final UrlRepository urlRepository;

    public UrlController(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }
    @PostMapping("/shorten-url")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest request, HttpServletRequest servletRequest) {
        String id;
        do {
            id = RandomStringUtils.randomAlphabetic(5,10);
        }while(urlRepository.existsById(id));

        urlRepository.save(new UrlEntity(id, request.url(), LocalDateTime.now().plusMinutes(1)));
        return ResponseEntity.ok(new ShortenUrlResponse(id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Void> redirect(@PathVariable("id") String id) {
        var url = urlRepository.findById(id);
        if(url.isEmpty() || url.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.get().getFullUrl()));
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

}
