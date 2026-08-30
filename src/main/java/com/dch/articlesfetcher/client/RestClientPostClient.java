package com.dch.articlesfetcher.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class RestClientPostClient implements PostClient{
    private static final Logger log = LoggerFactory.getLogger(RestClientPostClient.class);
    private static final String POST_PATH = "/posts";
    private final RestClient restClient;

    public RestClientPostClient(RestClient restClient) {
        this.restClient = restClient;
    }


    @Override
    public List<PostResponse> fetchPosts() {
        log.debug("Fetching posts from external API");
        List<PostResponse> posts = restClient.get()
                .uri(POST_PATH)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        log.info("Fetched {} posts from external API", posts == null ? 0 : posts.size());

        return posts == null ? List.of() : posts;
    }
}
