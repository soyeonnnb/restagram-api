package com.restgram.domain.feed.service;

import com.restgram.domain.feed.dto.response.UserFeedImageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedImageService {
    List<UserFeedImageResponse> getFeedImageList(Long userId, Pageable pageable);
    List<UserFeedImageResponse> getReviewImageList(Long userId, Pageable pageable);
}
