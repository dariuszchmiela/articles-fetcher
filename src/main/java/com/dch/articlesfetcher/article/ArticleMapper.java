package com.dch.articlesfetcher.article;

import com.dch.articlesfetcher.client.PostResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ArticleMapper {

    public Article toArticle(PostResponse post, Instant fetchedAt) {
        return new Article(post.id(), post.title(), post.body(), fetchedAt);
    }
}