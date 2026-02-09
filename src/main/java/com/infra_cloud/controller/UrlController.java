package com.infra_cloud.controller;

import com.infra_cloud.model.UrlMapping;
import com.infra_cloud.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UrlController {

    @Autowired
    private UrlService urlService;

    // POST /api/shorten - Generating shorten url
    // Shorten API - returns SAME short URL for SAME original URL
    @PostMapping("/shorten")// http://localhost:8080/api/shorten
    public ResponseEntity<?> shortenUrl(@RequestBody Map<String, String> request) {

        String originalUrl = request.get("url");
        String shortCode = urlService.generateShortUrl(originalUrl);

        Map<String, String> response = new HashMap<>();
        response.put("shortUrl", "http://localhost:8080/" + shortCode);

        return ResponseEntity.ok(response);
    }

    // REDIRECTION API - returns HTTP 302 redirect
    // ------------------------------
    @GetMapping("/redirect/{shortCode}")
    public RedirectView redirect(@PathVariable String shortCode) {

        String originalUrl = urlService.getOriginalUrl(shortCode);
        return new RedirectView(originalUrl);
    }
    // Returns original URL as text (no redirect)
    @GetMapping("/original/{shortCode}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortCode) {
        return ResponseEntity.ok(urlService.getOriginalUrl(shortCode));
    }

    // TOP 3 MOST SHORTENED DOMAINS
    @GetMapping("/metrics/top-domains")
    public ResponseEntity<Map<String, Integer>> getTopDomains() {
        return ResponseEntity.ok(urlService.getTopDomains());
    }
}
