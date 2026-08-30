package com.dch.articlesfetcher.article;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a.externalId from Article a where a.externalId in :externalIds")
    Set<Long> findExistingExternalIds(@Param("externalIds") Set<Long> externalIds);

    List<Article> findByReadAtIsNullOrderByExternalIdAsc(Limit limit);
}
