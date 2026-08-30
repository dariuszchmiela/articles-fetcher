package com.dch.articlesfetcher.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "articles.unread")
public record UnreadArticlesProperties(int limit) {}

