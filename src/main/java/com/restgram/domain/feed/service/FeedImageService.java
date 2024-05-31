package com.restgram.domain.feed.service;

import com.restgram.domain.feed.dto.response.UserFeedImageResponse;
import com.restgram.domain.feed.entity.Feed;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedImageService {
    List<UserFeedImageResponse> getFeedImageList(Long userId, Pageable pageable);
    List<UserFeedImageResponse> getReviewImageList(Long userId, Pageable pageable);
    void saveFeedImage(Feed feed, Integer idx, MultipartFile file);
}
