package com.saptak.trafficai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
@Setter
@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "custom-vision.prediction")
public class CustomVisionConfig {
    private String key;
    private String endpoint;
    private String projectId;
    private String publishedName;
    private double predictionThreshold;
    private boolean decoyMode;

    @Bean
    public WebClient getCustomVisionClient() {
        return WebClient.builder()
                .baseUrl(endpoint)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
