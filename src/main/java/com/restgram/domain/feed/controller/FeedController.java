package com.restgram.domain.feed.controller;

import com.restgram.domain.feed.dto.request.AddFeedRequest;
import com.restgram.domain.feed.dto.request.UpdateFeedRequest;
import com.restgram.domain.feed.dto.response.FeedCursorResponse;
import com.restgram.domain.feed.dto.response.FeedResponse;
import com.restgram.domain.feed.service.FeedService;
import com.restgram.global.exception.entity.ApiResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/feed")
@Slf4j
public class FeedController {

    private final FeedService feedService;

    // 게시물 작성
    @PostMapping
    public ResponseEntity<ApiResponse<?>> postFeed(Authentication authentication,
                                                   @RequestPart @Valid AddFeedRequest req,
                                                   @RequestPart(name = "images") List<MultipartFile> images) {
        Long userId = Long.parseLong(authentication.getName());
        feedService.addFeed(userId, req, images);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.CREATED);
    }

    // 게시물 삭제
    @DeleteMapping("/{feedId}")
    public ResponseEntity<ApiResponse<?>> deleteFeed(Authentication authentication, @PathVariable("feedId") Long feedId) {
        Long userId = Long.parseLong(authentication.getName());
        feedService.deleteFeed(userId, feedId);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
    }

    // 피드 수정은 글만 수정 가능
    @PatchMapping
    public ResponseEntity<?> updateFeed(Authentication authentication, @RequestBody @Valid UpdateFeedRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        feedService.updateFeed(userId, request);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
    }

    // 팔로우한+내 유저의 피드 가져오기
    @GetMapping
    public ResponseEntity<ApiResponse<List<FeedResponse>>> getFeeds(Authentication authentication,
                                                                    Pageable pageable) {
        Long userId = Long.parseLong(authentication.getName());
        List<FeedResponse> feedResponseList = feedService.getFeeds(userId, pageable);

        return new ResponseEntity<>(ApiResponse.createSuccess(feedResponseList), HttpStatus.OK);
    }

    // 팔로우한+내 유저의 피드 가져오기
    @GetMapping("/cursor")
    public ResponseEntity<ApiResponse<FeedCursorResponse>> getFeedsCursor(Authentication authentication, @RequestParam(value = "cursor-id", required = false) Long cursorId) {
        Long userId = Long.parseLong(authentication.getName());
        FeedCursorResponse feedResponseList = feedService.getFeedsCursor(userId, cursorId);

        return new ResponseEntity<>(ApiResponse.createSuccess(feedResponseList), HttpStatus.OK);
    }

    // 피드 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FeedResponse>>> searchFeed(Authentication authentication, @RequestParam("address-id") @Nullable Long addressId, @RequestParam("address-range") Integer addressRange, @RequestParam("query") String query, Pageable pageable) {
        Long userId = Long.parseLong(authentication.getName());
        List<FeedResponse> feedResponseList = feedService.searchFeeds(userId, addressId, addressRange, query, pageable);

        return new ResponseEntity<>(ApiResponse.createSuccess(feedResponseList), HttpStatus.OK);
    }
}
