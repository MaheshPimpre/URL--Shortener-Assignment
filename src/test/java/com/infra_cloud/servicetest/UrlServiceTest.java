package com.infra_cloud.servicetest;

import com.infra_cloud.controller.UrlController;
import com.infra_cloud.service.UrlService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UrlController.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlService service;

    // --------------------------
    // TEST 1: POST /api/shorten - Should shorten URL successfully
    // --------------------------
    @Test
    void testShortenUrlSuccess() throws Exception {

        Mockito.when(service.generateShortUrl("https://google.com"))
                .thenReturn("abc123");

        String requestBody = """
        {"url": "https://google.com"}
    """;

        mockMvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl")
                        .value("http://localhost:8080/abc123"));
    }


    // --------------------------
    // TEST 2: POST /api/shorten - SAME URL should return SAME short code
    // --------------------------
    @Test
    void testShortenUrlSameUrlReturnsSameShortCode() throws Exception {

        Mockito.when(service.generateShortUrl("https://example.com"))
                .thenReturn("same123");

        String requestBody = """
        {"url":"https://example.com"}
    """;

        mockMvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl")
                        .value("http://localhost:8080/same123"));
    }
    // --------------------------
    // TEST 3: POST /api/shorten - Missing URL field
    // --------------------------
    @Test
    void testShortenUrlMissingField() throws Exception {
        String requestBody = """
        {"wrongField": "123"} 
        """;

        Mockito.when(service.generateShortUrl(null))
                .thenThrow(new RuntimeException("URL missing"));

        mockMvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    // --------------------------
// TEST 4: GET /api/redirect/{shortCode} - Should redirect (302) to original URL
// --------------------------
    @Test
    void testRedirectShortUrlSuccess() throws Exception {

        Mockito.when(service.getOriginalUrl("abc123"))
                .thenReturn("https://google.com");

        mockMvc.perform(get("/api/redirect/abc123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://google.com"));
    }

    // --------------------------
// TEST 5: GET /api/redirect/{shortCode}- Should return 404 if shortCode is invalid
// --------------------------
    @Test
    void testRedirectShortUrlNotFound() throws Exception {

        Mockito.when(service.getOriginalUrl("invalid"))
                .thenThrow(new RuntimeException("URL not found!"));

        mockMvc.perform(get("/api/redirect/invalid"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("URL not found!"));
    }

    // --------------------------
// TEST 6: GET /api/original/{shortCode}- Should return original URL directly
// --------------------------
    @Test
    void testGetOriginalUrlSuccess() throws Exception {

        Mockito.when(service.getOriginalUrl("xyz789"))
                .thenReturn("https://twitter.com");

        mockMvc.perform(get("/api/original/xyz789"))
                .andExpect(status().isOk())
                .andExpect(content().string("https://twitter.com"));
    }

    // --------------------------
// TEST 7: GET /api/original/{shortCode}- Should return 404 when not found
// --------------------------
    @Test
    void testGetOriginalUrlNotFound() throws Exception {

        Mockito.when(service.getOriginalUrl("unknown"))
                .thenThrow(new RuntimeException("URL not found!"));

        mockMvc.perform(get("/api/original/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("URL not found!"));
    }
    // --------------------------
// TEST 8: GET /api/metrics/top-domains-Get Top 3 Domains
// --------------------------
    @Test
    void testGetTopDomains() throws Exception {

        Map<String, Integer> top3 = new LinkedHashMap<>();
        top3.put("udemy.com", 6);
        top3.put("youtube.com", 4);
        top3.put("wikipedia.org", 2);

        Mockito.when(service.getTopDomains()).thenReturn(top3);

        mockMvc.perform(get("/api/metrics/top-domains"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['udemy.com']").value(6))
                .andExpect(jsonPath("$.['youtube.com']").value(4))
                .andExpect(jsonPath("$.['wikipedia.org']").value(2));
    }


}