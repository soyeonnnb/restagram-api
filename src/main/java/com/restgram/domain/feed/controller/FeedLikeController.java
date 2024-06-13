package com.restgram.domain.feed.controller;

import com.restgram.domain.feed.dto.response.FeedResponse;
import com.restgram.domain.feed.service.FeedLikeService;
import com.restgram.global.exception.entity.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/feed/like")
@Slf4j
public class FeedLikeController {

    private final FeedLikeService feedLikeService;

    // 좋아요 누르기
    @PostMapping("/{feedId}")
    public ResponseEntity<ApiResponse<?>> postFeedLike(Authentication authentication, @PathVariable("feedId") Long feedId) {
        Long userId = Long.parseLong(authentication.getName());
        feedLikeService.postFeedLike(userId, feedId);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.CREATED);
    }

    // 좋아요 취소
    @DeleteMapping("/{feedId}")
    public ResponseEntity<?> deleteFeedLike(Authentication authentication, @PathVariable("feedId") Long feedId) {
        Long userId = Long.parseLong(authentication.getName());
        feedLikeService.deleteFeedLike(userId, feedId);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
    }

    // 좋아요한 피드 리스트
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<FeedResponse>>> getFeedLikeList(@PathVariable("userId") Long userId) {
        List<FeedResponse> feedResponseList = feedLikeService.getUsersFeedLikeList(userId);

        return new ResponseEntity<>(ApiResponse.createSuccess(feedResponseList), HttpStatus.OK);
    }

}
