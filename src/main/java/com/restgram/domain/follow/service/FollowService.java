package com.restgram.domain.follow.service;

import com.restgram.domain.follow.dto.response.FollowUserResponse;

import java.util.List;

public interface FollowService {

    void follow(Long userId, Long followingId);

    void deleteFollow(Long followerId, Long followingId);

    List<FollowUserResponse> getFollowerList(Long userId, Long followingId);

    List<FollowUserResponse> getFollowingList(Long userId, Long followerId);
}
