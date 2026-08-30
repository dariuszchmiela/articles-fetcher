package com.dch.articlesfetcher.article;

import org.springframework.stereotype.Component;

@Component
public class ArticleDtoMapper {

    public ArticleDto toDto(Article article) {
        return new ArticleDto(
                article.getId(),
                article.getExternalId(),
                article.getTitle(),
                article.getBody(),
                article.getFetchedAt()
        );
    }
}