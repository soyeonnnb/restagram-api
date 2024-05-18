package com.restgram.domain.feed.service;

import com.restgram.domain.feed.dto.request.AddFeedRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FeedService {
    void addFeed(Long userId, AddFeedRequest req, List<MultipartFile> images);
}
