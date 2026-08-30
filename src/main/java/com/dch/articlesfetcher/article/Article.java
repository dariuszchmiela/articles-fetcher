package com.dch.articlesfetcher.article;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false, unique = true)
    private Long externalId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String body;

    @Column(name = "fetched_at", nullable = false)
    private Instant fetchedAt;

    @Column(name = "read_at")
    private Instant readAt;

    protected Article() {
    }

    public Article(Long externalId, String title, String body, Instant fetchedAt) {
        this.externalId = externalId;
        this.title = title;
        this.body = body;
        this.fetchedAt = fetchedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getExternalId() {
        return externalId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Instant getFetchedAt() {
        return fetchedAt;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public void markAsRead(Instant readAt) {
        this.readAt = readAt;
    }
}