package com.dch.articlesfetcher.scheduler;

import com.dch.articlesfetcher.article.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ArticleFetchScheduler {
    private static final Logger log = LoggerFactory.getLogger(ArticleFetchScheduler.class);

    private final ArticleService articleService;

    public ArticleFetchScheduler(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Scheduled(initialDelayString = "${articles.fetch.initial-delay}",
            fixedDelayString = "${articles.fetch.delay}")
    public void fetchArticles() {
        log.info("Scheduled article fetch starting");
        try {
            int saved = articleService.fetchAndSaveNewArticles();
            log.info("Scheduled article fetch finished, saved {} articles", saved);
        } catch (RuntimeException e) {
            log.error("Scheduled article fetch failed", e);
        }
    }
}
