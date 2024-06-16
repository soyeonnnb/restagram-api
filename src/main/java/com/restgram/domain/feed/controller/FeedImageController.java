package com.restgram.domain.feed.controller;

import com.restgram.domain.feed.service.FeedImageService;
import com.restgram.global.entity.PaginationResponse;
import com.restgram.global.exception.entity.ApiResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/feed")
@Slf4j
public class FeedImageController {

    private final FeedImageService feedImageService;

    // 유저 피드 이미지 리스트 가져오기
    @GetMapping("/image/{userId}")
    public ResponseEntity<ApiResponse<PaginationResponse>> userFeedList(@PathVariable("userId") Long userId, @RequestParam("cursor-id") @Nullable Long cursorId) {
        PaginationResponse feedImageCursorResponse = feedImageService.getFeedImageList(userId, cursorId);

        return new ResponseEntity<>(ApiResponse.createSuccess(feedImageCursorResponse), HttpStatus.OK);
    }

    // 유저 리뷰 이미지 리스트 가져오기
    @GetMapping("/image/review/{userId}")
    public ResponseEntity<ApiResponse<PaginationResponse>> userReviewFeedList(@PathVariable("userId") Long userId, @RequestParam("cursor-id") @Nullable Long cursorId) {
        PaginationResponse feedImageCursorResponse = feedImageService.getReviewImageList(userId, cursorId);

        return new ResponseEntity<>(ApiResponse.createSuccess(feedImageCursorResponse), HttpStatus.OK);
    }

}
