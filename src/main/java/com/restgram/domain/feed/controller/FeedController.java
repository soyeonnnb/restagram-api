package com.restgram.domain.feed.controller;

import com.restgram.domain.feed.dto.request.AddFeedRequest;
import com.restgram.domain.feed.dto.request.UpdateFeedRequest;
import com.restgram.domain.feed.dto.response.FeedResponse;
import com.restgram.domain.feed.service.FeedService;
import com.restgram.global.entity.PaginationResponse;
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
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<?>> postFeed(Authentication authentication,
                                                   @RequestPart(name = "req") @Valid AddFeedRequest req,
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
    public ResponseEntity<ApiResponse<PaginationResponse>> getFeedsCursor(Authentication authentication, @RequestParam(value = "cursor-id", required = false) Long cursorId) {
        Long userId = Long.parseLong(authentication.getName());
        PaginationResponse feedResponseList = feedService.getFeedsCursor(userId, cursorId);

        return new ResponseEntity<>(ApiResponse.createSuccess(feedResponseList), HttpStatus.OK);
    }

    // 피드 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PaginationResponse>> searchFeed(Authentication authentication, @RequestParam("address-id") @Nullable Long addressId, @RequestParam("address-range") Integer addressRange, @RequestParam("query") String query, @RequestParam("cursor-id") @Nullable Long cursorId) {
        Long userId = Long.parseLong(authentication.getName());
        PaginationResponse feedResponseList = feedService.searchFeeds(userId, addressId, addressRange, query, cursorId);

        return new ResponseEntity<>(ApiResponse.createSuccess(feedResponseList), HttpStatus.OK);
    }

    // 유저의 특정 피드 전/후 가져오기
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<PaginationResponse>> searchPrevNextFeed(Authentication authentication, @PathVariable("userId") Long userId, @RequestParam("cursor-id") Long cursorId) {
        Long loginUserId = Long.parseLong(authentication.getName());
        PaginationResponse feedResponseList = feedService.searchUserFeed(loginUserId, userId, cursorId);

        return new ResponseEntity<>(ApiResponse.createSuccess(feedResponseList), HttpStatus.OK);
    }


}
