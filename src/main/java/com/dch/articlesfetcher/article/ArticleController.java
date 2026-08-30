package com.dch.articlesfetcher.article;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @PostMapping("/{id}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAsRead(@PathVariable Long id) {
        articleService.markAsRead(id);
    }
}