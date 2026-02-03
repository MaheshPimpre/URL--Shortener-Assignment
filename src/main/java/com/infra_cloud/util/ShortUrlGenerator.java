package com.infra_cloud.util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Generates short URL using Base64 encoding of hash.
 */
@Component
public class ShortUrlGenerator {
    public static String generateShortUrl(String url) {
        // Simple hash: encode URL and take first 6 chars
        return Base64.getUrlEncoder()
                .encodeToString(url.getBytes(StandardCharsets.UTF_8))
                .substring(0, 6);
    }
}
