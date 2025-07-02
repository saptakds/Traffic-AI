package com.saptak.trafficai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "traffic-ai.signal")
public class SignalConfig {
    private long roundRobinIntervalMs;
    private long priorityTimeoutMs;
}
