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
        if (following_id == follower_id) throw new RestApiException(FollowErrorCode.FOLLOW_FOLLOWING_IS_THE_SAME);
        User follower = userRepository.findById(follower_id).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        User following = userRepository.findById(following_id).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        if (followRepository.existsByFollowerAndFollowing(follower, following)) throw new RestApiException(FollowErrorCode.ALREADY_FOLLOWED);
        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();
        followRepository.save(follow);
    }

    @Override
    @Transactional
    public void delete_follow(Long follower_id, Long id) {
        Follow follow = followRepository.findById(id).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        if (follow.getFollower().getId() != follower_id) throw new RestApiException(UserErrorCode.USER_MISMATCH);
        followRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowUserResponse> getFollowerList(Long user_id, Long following_id) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        User following = userRepository.findById(following_id).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
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
        User user = userRepository.findById(user_id).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        User follower = userRepository.findById(follower_id).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        List<Follow> followList = followRepository.findByFollower(follower);
        List<FollowUserResponse> followUserResponseList = new ArrayList<>();
        for(Follow follow : followList) {
            followUserResponseList.add(FollowUserResponse.of(follow.getId(), follow.getFollowing(), followRepository.existsByFollowerAndFollowing(user, follow.getFollowing())));
        }
        return followUserResponseList;
    }
}
