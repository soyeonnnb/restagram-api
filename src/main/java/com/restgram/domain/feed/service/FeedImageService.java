package com.restgram.domain.feed.service;

import com.restgram.domain.feed.dto.response.UserFeedImageResponse;

import java.util.List;

public interface FeedImageService {
    List<UserFeedImageResponse> getFeedImageList(Long userId);
    List<UserFeedImageResponse> getReviewImageList(Long userId);
}
