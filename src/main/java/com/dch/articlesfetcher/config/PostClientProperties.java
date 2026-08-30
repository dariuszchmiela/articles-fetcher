package com.dch.articlesfetcher.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "articles.client")
public record PostClientProperties(
        String baseUrl,
        Duration connectionTimeOut,
        Duration readTimeOut
) {
}
