package com.restgram.domain.feed.service;

import com.restgram.domain.feed.entity.Feed;
import com.restgram.global.entity.PaginationResponse;
import org.springframework.web.multipart.MultipartFile;


public interface FeedImageService {
    PaginationResponse getFeedImageList(Long userId, Long cursorId);

    PaginationResponse getReviewImageList(Long userId, Long cursorId);

    void saveFeedImage(Feed feed, Integer idx, MultipartFile file);
}
