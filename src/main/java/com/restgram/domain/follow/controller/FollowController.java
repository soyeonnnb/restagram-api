package com.restgram.domain.follow.controller;

import com.restgram.domain.follow.dto.response.FollowUserResponse;
import com.restgram.domain.follow.service.FollowService;
import com.restgram.global.exception.entity.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/follow")
@Slf4j
public class FollowController {

  private final FollowService followService;

  // 팔로우 걸기
  @PostMapping("/{following_id}")
  public ResponseEntity<ApiResponse<?>> postFollow(Authentication authentication,
      @PathVariable("following_id") Long followingId) {
    Long userId = Long.parseLong(authentication.getName());
    followService.follow(userId, followingId);

    return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.CREATED);
  }

  // 팔로우 취소
  @DeleteMapping("/{follow_id}")
  public ResponseEntity<ApiResponse<?>> deleteFollow(Authentication authentication,
      @PathVariable("follow_id") Long followId) {
    Long userId = Long.parseLong(authentication.getName());
    followService.deleteFollow(userId, followId);

    return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
  }

  // 팔로우 리스트 가져오기
  @GetMapping("/following/{follower_id}")
  public ResponseEntity<ApiResponse<List<FollowUserResponse>>> getFollowingList(
      Authentication authentication, @PathVariable("follower_id") Long followerId) {
    Long userId = Long.parseLong(authentication.getName());
    List<FollowUserResponse> followUserResponseList = followService.getFollowingList(userId,
        followerId);

    return new ResponseEntity<>(ApiResponse.createSuccess(followUserResponseList), HttpStatus.OK);
  }

  // 팔로잉 리스트 가져오기
  @GetMapping("/follower/{following_id}")
  public ResponseEntity<ApiResponse<List<FollowUserResponse>>> getFollowerList(
      Authentication authentication, @PathVariable("following_id") Long following_id) {
    Long userId = Long.parseLong(authentication.getName());
    List<FollowUserResponse> followUserResponseList = followService.getFollowerList(userId,
        following_id);

    return new ResponseEntity<>(ApiResponse.createSuccess(followUserResponseList), HttpStatus.OK);
  }


}
