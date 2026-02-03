package com.infra_cloud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Represents mapping between original and shortened URL.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UrlMapping {


    private Long id;

    private String originalUrl;

    private String shortCode;
}
