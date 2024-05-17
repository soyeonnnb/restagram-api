package com.restgram.domain.follow.service;

import com.restgram.domain.follow.dto.response.FollowUserResponse;

import java.util.List;

public interface FollowService {
    void follow(Long follower_id, Long following_id);
    void delete_follow(Long follower_id, Long id);
    List<FollowUserResponse> getFollowerList(Long user_id, Long following_id);
    List<FollowUserResponse> getFollowingList(Long user_id, Long follower_id);
}
