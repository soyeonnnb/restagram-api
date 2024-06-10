package com.restgram.domain.feed.service;

import com.restgram.domain.feed.dto.response.FeedImageCursorResponse;
import com.restgram.domain.feed.entity.Feed;
import org.springframework.web.multipart.MultipartFile;


public interface FeedImageService {
    FeedImageCursorResponse getFeedImageList(Long userId, Long cursorId);
    FeedImageCursorResponse getReviewImageList(Long userId, Long cursorId);
    void saveFeedImage(Feed feed, Integer idx, MultipartFile file);
}
