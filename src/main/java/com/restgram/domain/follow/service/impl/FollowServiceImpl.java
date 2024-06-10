package com.restgram.domain.follow.service.impl;

import com.restgram.domain.follow.dto.response.FollowUserResponse;
import com.restgram.domain.follow.entity.Follow;
import com.restgram.domain.follow.repository.FollowRepository;
import com.restgram.domain.follow.service.FollowService;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CommonErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;


    @Override
    @Transactional
    public void follow(Long follower_id, Long following_id) {
        if (following_id == follower_id) throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
        User follower = userRepository.findById(follower_id).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        User following = userRepository.findById(following_id).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        if (followRepository.existsByFollowerAndFollowing(follower, following)) throw new RestApiException(CommonErrorCode.ENTITY_ALREADY_EXISTS);
        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();
        followRepository.save(follow);
    }

    @Override
    @Transactional
    public void delete_follow(Long follower_id, Long id) {
        Follow follow = followRepository.findById(id).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND));
        if (follow.getFollower().getId() != follower_id) throw new RestApiException(UserErrorCode.USER_MISMATCH);
        followRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowUserResponse> getFollowerList(Long user_id, Long following_id) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        User following = userRepository.findById(following_id).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        List<Follow> followList = followRepository.findByFollowing(following);
        List<FollowUserResponse> followUserResponseList = new ArrayList<>();
        for(Follow follow : followList) {
            followUserResponseList.add(FollowUserResponse.of(follow.getId(), follow.getFollower(), followRepository.existsByFollowerAndFollowing(user, follow.getFollower())));
        }
        return followUserResponseList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowUserResponse> getFollowingList(Long user_id, Long follower_id) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        User follower = userRepository.findById(follower_id).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        List<Follow> followList = followRepository.findByFollower(follower);
        List<FollowUserResponse> followUserResponseList = new ArrayList<>();
        for(Follow follow : followList) {
            followUserResponseList.add(FollowUserResponse.of(follow.getId(), follow.getFollowing(), followRepository.existsByFollowerAndFollowing(user, follow.getFollowing())));
        }
        return followUserResponseList;
    }
}
