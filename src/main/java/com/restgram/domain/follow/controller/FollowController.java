package com.restgram.domain.follow.controller;

import com.restgram.domain.follow.dto.response.FollowUserResponse;
import com.restgram.domain.follow.service.FollowService;
import com.restgram.global.exception.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
@Slf4j
public class FollowController {

    private final FollowService followService;

    // 팔로우 걸기
    @PostMapping("/{following_id}")
    public CommonResponse postFollow(Authentication authentication, @PathVariable Long following_id) {
        Long user_id = Long.parseLong(authentication.getName());
        followService.follow(user_id, following_id);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("Follow success")
                .build();
    }

    // 팔로우 취소
    @DeleteMapping("/{id}")
    public CommonResponse deleteFollow(Authentication authentication, @PathVariable Long id) {
        Long user_id = Long.parseLong(authentication.getName());
        followService.delete_follow(user_id, id);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Follow cancel success")
                .build();
    }

    @GetMapping("/following/{follower_id}")
    public CommonResponse getFollowingList(Authentication authentication, @PathVariable Long follower_id) {
        Long user_id = Long.parseLong(authentication.getName());
        List<FollowUserResponse> followUserResponseList = followService.getFollowingList(user_id, follower_id);
        return CommonResponse.builder()
                .success(true)
                .data(followUserResponseList)
                .code(HttpStatus.OK.value())
                .message("팔로잉 리스트 가져오기")
                .build();
    }

    @GetMapping("/follower/{following_id}")
    public CommonResponse getFollowerList(Authentication authentication, @PathVariable Long following_id) {
        Long user_id = Long.parseLong(authentication.getName());
        List<FollowUserResponse> followUserResponseList = followService.getFollowerList(user_id, following_id);
        return CommonResponse.builder()
                .success(true)
                .data(followUserResponseList)
                .code(HttpStatus.OK.value())
                .message("팔로워 리스트 가져오기")
                .build();
    }


}
