package com.restgram.domain.feed.service;

import com.restgram.domain.feed.dto.response.FeedResponse;

import java.util.List;

public interface FeedLikeService {
    void postFeedLike(Long userId, Long feedId);
    void deleteFeedLike(Long userId, Long feedId);
    List<FeedResponse> getUsersFeedLikeList(Long userId);
}
