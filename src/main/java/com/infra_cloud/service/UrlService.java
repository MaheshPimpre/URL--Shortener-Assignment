package com.infra_cloud.service;

import com.infra_cloud.store.UrlStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UrlService {

    @Autowired
    private UrlStore urlStore;

    // Create short url of original url- Return same short URL if original URL already exists
    public String generateShortUrl(String originalUrl) {

        // Return same short URL if already exists
        if (urlStore.containsOriginal(originalUrl)) {
            return urlStore.getShortFromOriginal(originalUrl);
        }

        // Generate unique short code
        String shortCode = generateUniqueCode();
        urlStore.save(shortCode, originalUrl);

        // Extract domain & update count
        String domain = getDomainName(originalUrl);
        urlStore.incrementDomain(domain);

        return shortCode;
    }

    public String getOriginalUrl(String code) {
        String url = urlStore.getOriginalFromShort(code);
        if (url == null) {
            throw new RuntimeException("URL not found!");
        }
        return url;
    }

    //Return top 3 most shortened domains
    public Map<String, Integer> getTopDomains() {
        return urlStore.getDomainCount()
                .entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue()) // descending
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }
   // Extract domain from URL
    private String getDomainName(String url) {
        try {
            URI uri = URI.create(url);
            return uri.getHost().replace("www.", "");
        } catch (Exception e) {
            throw new RuntimeException("Invalid URL");
        }
    }
    private String generateUniqueCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        String code;

        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            code = sb.toString();
        } while (urlStore.containsShort(code));

        return code;
    }

}
