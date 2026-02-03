package com.infra_cloud.store;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UrlStore {

    private final ConcurrentHashMap<String, String> shortToOriginal = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> originalToShort = new ConcurrentHashMap<>();

    // NEW: Domain count map (domain → times shortened)
    private final ConcurrentHashMap<String, Integer> domainCount = new ConcurrentHashMap<>();

    public boolean containsOriginal(String originalUrl) {
        return originalToShort.containsKey(originalUrl);
    }

    public boolean containsShort(String code) {
        return shortToOriginal.containsKey(code);
    }

    public void save(String code, String originalUrl) {
        shortToOriginal.put(code, originalUrl);
        originalToShort.put(originalUrl, code);
    }

    public String getShortFromOriginal(String originalUrl) {
        return originalToShort.get(originalUrl);
    }

    public String getOriginalFromShort(String code) {
        return shortToOriginal.get(code);
    }

    // NEW: Increase domain count
    public void incrementDomain(String domain) {
        domainCount.merge(domain, 1, Integer::sum);
    }

    public Map<String, Integer> getDomainCount() {
        return domainCount;
    }
}
