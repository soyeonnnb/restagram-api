package com.restgram.domain.feed.controller;

import com.restgram.domain.feed.dto.response.UserFeedImageResponse;
import com.restgram.domain.feed.service.FeedImageService;
import com.restgram.global.exception.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feed")
@Slf4j
public class FeedImageController {

    private final FeedImageService feedImageService;

    @GetMapping("/image/{userId}")
    public CommonResponse userFeedList(@PathVariable Long userId, @PageableDefault(size = 30) Pageable pageable) {
        List<UserFeedImageResponse> feedImageResponses = feedImageService.getFeedImageList(userId, pageable);
        return CommonResponse.builder()
                .success(true)
                .data(feedImageResponses)
                .code(HttpStatus.OK.value())
                .message("피드 이미지 리스트 가져오기")
                .build();
    }
    @GetMapping("/image/review/{userId}")
    public CommonResponse userReviewFeedList(@PathVariable Long userId, @PageableDefault(size = 30) Pageable pageable) {
        List<UserFeedImageResponse> feedImageResponses = feedImageService.getReviewImageList(userId, pageable);
        return CommonResponse.builder()
                .success(true)
                .data(feedImageResponses)
                .code(HttpStatus.OK.value())
                .message("리뷰 이미지 리스트 가져오기")
                .build();
    }

}
