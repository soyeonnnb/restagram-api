package com.restgram.domain.feed.service.impl;

import com.restgram.domain.feed.dto.response.FeedResponse;
import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.feed.entity.FeedLike;
import com.restgram.domain.feed.repository.FeedImageRepository;
import com.restgram.domain.feed.repository.FeedLikeRepository;
import com.restgram.domain.feed.repository.FeedRepository;
import com.restgram.domain.feed.service.FeedLikeService;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.FeedErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedLikeServiceImpl implements FeedLikeService {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final FeedImageRepository feedImageRepository;
    private final FeedLikeRepository feedLikeRepository;

    @Override
    @Transactional
    public void postFeedLike(Long userId, Long feedId) {
        // 유저
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));
        // 피드
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new RestApiException(FeedErrorCode.INVALID_FEED_ID,
                        "피드ID가 유효하지 않습니다. [피드ID=" + feedId + "]"));
        // 이전에 좋아요 했는지 확인
        if (feedLikeRepository.existsByFeedAndUser(feed, user)) {
            throw new RestApiException(FeedErrorCode.ALREADY_FEED_LIKE,
                    "이미 좋아요 한 피드입니다. [로그인 사용자ID=" + userId + ", 피드ID=" + feedId + "]");
        }

        FeedLike feedLike = FeedLike.builder().user(user).feed(feed).build();
        feedLikeRepository.save(feedLike);
    }

    @Override
    @Transactional
    public void deleteFeedLike(Long userId, Long feedId) {
        // 유저
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));
        // 피드
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new RestApiException(FeedErrorCode.INVALID_FEED_ID,
                        "피드ID가 유효하지 않습니다. [피드ID=" + feedId + "]"));

        feedLikeRepository.deleteByFeedAndUser(feed, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedResponse> getUsersFeedLikeList(Long userId) {
        // 유저
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));
        // 좋아요 한 피드 리스트
        List<FeedLike> feedList = feedLikeRepository.findAlllByUserOrderByCreatedAtDesc(user);

        return feedList.stream().map(feedLike -> FeedResponse.of(feedLike.getFeed(),
                feedImageRepository.findAllByFeedOrderByNumber(feedLike.getFeed()), true)).collect(Collectors.toList());
    }
}
