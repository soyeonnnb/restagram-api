package com.restgram.domain.follow.service.impl;

import com.restgram.domain.follow.dto.response.FollowUserResponse;
import com.restgram.domain.follow.entity.Follow;
import com.restgram.domain.follow.repository.FollowRepository;
import com.restgram.domain.follow.service.FollowService;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.FollowErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;


    @Override
    @Transactional
    public void follow(Long userId, Long followingId) {
        if (followingId == userId) {
            throw new RestApiException(FollowErrorCode.FOLLOW_FOLLOWING_IS_THE_SAME,
                    "팔로워 사용자ID와 팔로잉 사용자ID가 일치합니다. [아이디=" + userId + "]");
        }

        User follower = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RestApiException(FollowErrorCode.INVALID_FOLLOWING_ID,
                        "팔로잉 사용자ID가 유효하지 않습니다. [팔로잉 사용자ID=" + followingId + "]"));

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new RestApiException(FollowErrorCode.ALREADY_FOLLOWED,
                    "이미 팔로우한 사용자입니다. [로그인 사용자ID=" + userId + ", 팔로잉 사용자ID=" + followingId + "]");
        }

        followRepository.save(Follow.builder().follower(follower).following(following).build());
    }

    @Override
    @Transactional
    public void deleteFollow(Long userId, Long followingId) {
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));

        User followUser = userRepository.findById(followingId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID,
                "사용자ID가 유효하지 않습니다. [사용자ID=" + followingId + "]"));

        followRepository.deleteByFollowerAndFollowing(loginUser, followUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowUserResponse> getFollowerList(Long userId, Long followingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RestApiException(FollowErrorCode.INVALID_FOLLOWING_ID,
                        "팔로잉 사용자ID가 유효하지 않습니다. [팔로잉 사용자ID=" + followingId + "]"));

        List<Follow> followList = followRepository.findByFollowing(following);

        return followList.stream()
                .map(follow -> FollowUserResponse.of(follow.getId(), follow.getFollower(),
                        followRepository.existsByFollowerAndFollowing(user, follow.getFollower()))).collect(
                        Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowUserResponse> getFollowingList(Long userId, Long followerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RestApiException(FollowErrorCode.INVALID_FOLLOWER_ID,
                        "팔로워 사용자ID가 유효하지 않습니다. [팔로워 사용자ID=" + followerId + "]"));

        List<Follow> followList = followRepository.findByFollower(follower);

        return followList.stream()
                .map(follow -> FollowUserResponse.of(follow.getId(), follow.getFollowing(),
                        followRepository.existsByFollowerAndFollowing(user, follow.getFollowing()))).collect(
                        Collectors.toList());
    }
}
