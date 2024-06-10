package com.restgram.domain.feed.controller;

import com.restgram.domain.feed.dto.response.FeedImageCursorResponse;
import com.restgram.domain.feed.service.FeedImageService;
import com.restgram.global.exception.entity.CommonResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/feed")
@Slf4j
public class FeedImageController {

    private final FeedImageService feedImageService;

    // 유저 피드 이미지 리스트 가져오기
    @GetMapping("/image/{userId}")
    public CommonResponse userFeedList(@PathVariable Long userId, @RequestParam("cursorId") @Nullable Long cursorId) {
        FeedImageCursorResponse feedImageResponses = feedImageService.getFeedImageList(userId, cursorId);
        return CommonResponse.builder()
                .success(true)
                .data(feedImageResponses)
                .code(HttpStatus.OK.value())
                .message("피드 이미지 리스트 가져오기")
                .build();
    }

    // 유저 리뷰 이미지 리스트 가져오기
    @GetMapping("/image/review/{userId}")
    public CommonResponse userReviewFeedList(@PathVariable Long userId, @RequestParam("cursorId") @Nullable Long cursorId) {
        FeedImageCursorResponse feedImageResponses = feedImageService.getReviewImageList(userId, cursorId);
        return CommonResponse.builder()
                .success(true)
                .data(feedImageResponses)
                .code(HttpStatus.OK.value())
                .message("리뷰 이미지 리스트 가져오기")
                .build();
    }

}
