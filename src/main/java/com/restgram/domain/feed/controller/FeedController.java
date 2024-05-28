package com.restgram.domain.feed.controller;

import com.restgram.domain.feed.dto.request.AddFeedRequest;
import com.restgram.domain.feed.dto.request.UpdateFeedRequest;
import com.restgram.domain.feed.dto.response.FeedResponse;
import com.restgram.domain.feed.dto.response.UserFeedImageResponse;
import com.restgram.domain.feed.service.FeedService;
import com.restgram.global.exception.entity.CommonResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feed")
@Slf4j
public class FeedController {

    private final FeedService feedService;

    @PostMapping
    public CommonResponse postFeed(Authentication authentication,
                                   @RequestPart @Valid AddFeedRequest req,
                                   @RequestPart(name = "images") List<MultipartFile> images) {
        Long userId = Long.parseLong(authentication.getName());
        feedService.addFeed(userId, req, images);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("피드가 정상적으로 업로드되었습니다.")
                .build();
    }

    @DeleteMapping("/{feedId}")
    public CommonResponse deleteFeed(Authentication authentication, @PathVariable Long feedId) {
        Long userId = Long.parseLong(authentication.getName());
        feedService.deleteFeed(userId, feedId);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("피드가 정상적으로 삭제되었습니다.")
                .build();
    }

    // 피드 수정은 글만 수정 가능
    @PatchMapping
    public CommonResponse updateFeed(Authentication authentication, @RequestBody UpdateFeedRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        feedService.updateFeed(userId, request);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("피드가 정상적으로 수정되었습니다.")
                .build();
    }

    // 팔로우한+내 유저의 피드 가져오기
    @GetMapping
    public CommonResponse getFeeds(Authentication authentication,
                                   Pageable pageable) {
        Long userId = Long.parseLong(authentication.getName());
        List<FeedResponse> feedResponseList = feedService.getFeeds(userId, pageable);
        return CommonResponse.builder()
                .success(true)
                .data(feedResponseList)
                .code(HttpStatus.OK.value())
                .message("팔로우한 유저 피드 가져오기")
                .build();
    }

    // 피드 검색
    @GetMapping("/search")
    public CommonResponse searchFeed(Authentication authentication, @RequestParam("addressId") @Nullable Long addressId, @RequestParam("addressRange") Integer addressRange, @RequestParam("query") String query, Pageable pageable) {
        Long userId = Long.parseLong(authentication.getName());
        List<FeedResponse> feedResponseList = feedService.searchFeeds(userId, addressId, addressRange, query, pageable);
        return CommonResponse.builder()
                .success(true)
                .data(feedResponseList)
                .code(HttpStatus.OK.value())
                .message("피드 검색하기")
                .build();
    }

}
