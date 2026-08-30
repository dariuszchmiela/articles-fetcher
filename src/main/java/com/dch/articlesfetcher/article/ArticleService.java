package com.dch.articlesfetcher.article;

import com.dch.articlesfetcher.client.PostClient;
import com.dch.articlesfetcher.client.PostResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final PostClient postClient;
    private final ArticleRepository articleRepository;
    private final Clock clock;

    public ArticleService(PostClient postClient, ArticleRepository articleRepository, Clock clock) {
        this.postClient = postClient;
        this.articleRepository = articleRepository;
        this.clock = clock;
    }

    @Transactional
    public int fetchAndSaveNewArticles() {
        List<PostResponse> posts = postClient.fetchPosts();
        if (posts.isEmpty()) {
            log.info("No posts fetched from external API");
            return 0;
        }

        Set<Long> fetchedIds = extractExternalIds(posts);
        Set<Long> existingIds = articleRepository.findExistingExternalIds(fetchedIds);
        List<Article> newArticles = toNewArticles(posts, existingIds);

        articleRepository.saveAll(newArticles);
        log.info("Saved {} new articles ({} fetched, {} already existed)",
                newArticles.size(), posts.size(), existingIds.size());
        return newArticles.size();
    }

    private Set<Long> extractExternalIds(List<PostResponse> posts) {
        return posts.stream()
                .map(PostResponse::id)
                .collect(Collectors.toSet());
    }

    private List<Article> toNewArticles(List<PostResponse> posts, Set<Long> existingIds) {
        Instant fetchedAt = Instant.now(clock);
        return posts.stream()
                .filter(post -> !existingIds.contains(post.id()))
                .map(post -> new Article(post.id(), post.title(), post.body(), fetchedAt))
                .toList();
    }
}