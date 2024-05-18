package com.restgram.domain.feed.service;

import com.restgram.domain.feed.dto.request.AddFeedRequest;
import com.restgram.domain.feed.dto.response.FeedResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedService {
    void addFeed(Long userId, AddFeedRequest req, List<MultipartFile> images);
    List<FeedResponse> getFeeds(Long userId);
}
