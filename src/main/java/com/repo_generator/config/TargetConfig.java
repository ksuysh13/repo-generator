package com.repo_generator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "target")
public class TargetConfig {
    private String name;
    private String token;
    private String userId;
    private String userName;
    private String url;
    private String api;
}
