package com.dch.articlesfetcher.client;

import com.dch.articlesfetcher.config.PostClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;


@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient postRestClient(RestClient.Builder builder, PostClientProperties properties) {
        return builder
                .baseUrl(properties.baseUrl())
                .build();
    }
}
