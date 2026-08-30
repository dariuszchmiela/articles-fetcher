package com.dch.articlesfetcher.article;

public class ArticleNotFoundException extends RuntimeException {

    public ArticleNotFoundException(Long articleId) {
        super("Article not found: %d".formatted(articleId));
    }
}