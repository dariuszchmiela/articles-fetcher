package com.dch.articlesfetcher.article;

import java.time.Instant;

public record ArticleDto(
        Long id,
        Long externalId,
        String title,
        String body,
        Instant fetchedAt
) {
}