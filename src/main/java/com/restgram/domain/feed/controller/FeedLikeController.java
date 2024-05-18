package com.restgram.domain.feed.controller;

import com.restgram.domain.feed.dto.response.FeedResponse;
import com.restgram.domain.feed.service.FeedLikeService;
import com.restgram.domain.feed.service.FeedService;
import com.restgram.global.exception.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feed/like")
@Slf4j
public class FeedLikeController {

    private final FeedLikeService feedLikeService;

    // 좋아요 누르기
    @PostMapping("/{feedId}")
    public CommonResponse postFeedLike(Authentication authentication, @PathVariable Long feedId) {
        Long userId = Long.parseLong(authentication.getName());
        feedLikeService.postFeedLike(userId, feedId);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("피드 좋아요")
                .build();
    }

    // 좋아요 취소
    @DeleteMapping("/{feedId}")
    public CommonResponse deleteFeedLike(Authentication authentication, @PathVariable Long feedId) {
        Long userId = Long.parseLong(authentication.getName());
        feedLikeService.deleteFeedLike(userId, feedId);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("피드 좋아요 취소")
                .build();
    }

    // 좋아요한 피드 리스트
    @GetMapping("/{userId}")
    public CommonResponse getFeedLikeList(@PathVariable Long userId) {
        List<FeedResponse> feedResponseList = feedLikeService.getUsersFeedLikeList(userId);
        return CommonResponse.builder()
                .success(true)
                .data(feedResponseList)
                .code(HttpStatus.OK.value())
                .message("피드 좋아요 리스트 가져오기")
                .build();
    }

}
