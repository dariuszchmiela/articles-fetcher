package com.dch.articlesfetcher.client;

public record PostResponse(
        Long id,
        Long userId,
        String title,
        String body
) {
}
