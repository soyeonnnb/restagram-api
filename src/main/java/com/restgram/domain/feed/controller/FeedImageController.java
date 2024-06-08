package com.restgram.domain.feed.controller;

import com.restgram.domain.feed.dto.response.FeedImageCursorResponse;
import com.restgram.domain.feed.dto.response.UserFeedImageResponse;
import com.restgram.domain.feed.service.FeedImageService;
import com.restgram.global.exception.entity.CommonResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feed")
@Slf4j
public class FeedImageController {

    private final FeedImageService feedImageService;

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
