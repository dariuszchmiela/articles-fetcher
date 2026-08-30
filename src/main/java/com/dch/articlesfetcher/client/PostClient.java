package com.dch.articlesfetcher.client;

import java.util.List;

public interface PostClient {
    List<PostResponse> fetchPosts();
}
