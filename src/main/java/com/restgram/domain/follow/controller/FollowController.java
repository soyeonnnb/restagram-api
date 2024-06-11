package com.restgram.domain.follow.controller;

import com.restgram.domain.follow.dto.response.FollowUserResponse;
import com.restgram.domain.follow.service.FollowService;
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
@RequestMapping("/api/v1/follow")
@Slf4j
public class FollowController {

    private final FollowService followService;

    // 팔로우 걸기
    @PostMapping("/{following_id}")
    public ResponseEntity<ApiResponse<?>> postFollow(Authentication authentication, @PathVariable Long following_id) {
        Long user_id = Long.parseLong(authentication.getName());
        followService.follow(user_id, following_id);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.CREATED);
    }

    // 팔로우 취소
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteFollow(Authentication authentication, @PathVariable Long id) {
        Long user_id = Long.parseLong(authentication.getName());
        followService.delete_follow(user_id, id);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
    }

    // 팔로우 리스트 가져오기
    @GetMapping("/following/{follower_id}")
    public ResponseEntity<ApiResponse<List<FollowUserResponse>>> getFollowingList(Authentication authentication, @PathVariable Long follower_id) {
        Long user_id = Long.parseLong(authentication.getName());
        List<FollowUserResponse> followUserResponseList = followService.getFollowingList(user_id, follower_id);

        return new ResponseEntity<>(ApiResponse.createSuccess(followUserResponseList), HttpStatus.OK);
    }

    // 팔로잉 리스트 가져오기
    @GetMapping("/follower/{following_id}")
    public ResponseEntity<ApiResponse<List<FollowUserResponse>>> getFollowerList(Authentication authentication, @PathVariable Long following_id) {
        Long user_id = Long.parseLong(authentication.getName());
        List<FollowUserResponse> followUserResponseList = followService.getFollowerList(user_id, following_id);

        return new ResponseEntity<>(ApiResponse.createSuccess(followUserResponseList), HttpStatus.OK);
    }


}
