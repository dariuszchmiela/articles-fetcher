package com.dch.articlesfetcher.article;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleDtoMapper articleDtoMapper;

    public ArticleController(ArticleService articleService, ArticleDtoMapper articleDtoMapper) {
        this.articleService = articleService;
        this.articleDtoMapper = articleDtoMapper;
    }

    @GetMapping("/unread")
    public List<ArticleDto> getUnreadArticles() {
        return articleService.getUnreadArticles().stream()
                .map(articleDtoMapper::toDto)
                .toList();
    }
}