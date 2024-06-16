package com.restgram.domain.feed.service;

import com.restgram.domain.feed.dto.request.AddFeedRequest;
import com.restgram.domain.feed.dto.request.UpdateFeedRequest;
import com.restgram.domain.feed.dto.response.FeedResponse;
import com.restgram.global.entity.PaginationResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedService {
    void addFeed(Long userId, AddFeedRequest req, List<MultipartFile> images);

    List<FeedResponse> getFeeds(Long userId, Pageable pageable);

    PaginationResponse searchFeeds(Long userId, Long addressId, Integer addressRange, String query, Long cursorId);

    void deleteFeed(Long userId, Long feedId);

    void updateFeed(Long userId, UpdateFeedRequest request);

    PaginationResponse getFeedsCursor(Long userId, Long cursorId);
}
