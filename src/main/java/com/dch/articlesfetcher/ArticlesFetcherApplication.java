package com.dch.articlesfetcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ArticlesFetcherApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticlesFetcherApplication.class, args);
    }
}
