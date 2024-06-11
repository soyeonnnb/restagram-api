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

import java.util.ArrayList;
import java.util.List;

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
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        // 피드
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new RestApiException(FeedErrorCode.INVALID_FEED_ID));
        // 이전에 좋아요 했는지 확인
        if (feedLikeRepository.existsByFeedAndUser(feed, user)) throw new RestApiException(FeedErrorCode.ALREADY_FEED_LIKE);
        FeedLike feedLike = FeedLike.builder()
                .user(user)
                .feed(feed)
                .build();
        feedLikeRepository.save(feedLike);
    }

    @Override
    @Transactional
    public void deleteFeedLike(Long userId, Long feedId) {
        // 유저
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        // 피드
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new RestApiException(FeedErrorCode.INVALID_FEED_ID));
        feedLikeRepository.deleteByFeedAndUser(feed, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedResponse> getUsersFeedLikeList(Long userId) {
        // 유저
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        List<FeedLike> feedList = feedLikeRepository.findAlllByUserOrderByCreatedAtDesc(user);
        List<FeedResponse> feedResponseList = new ArrayList<>();
        for(FeedLike feedLike : feedList) {
            feedResponseList.add(FeedResponse.of(feedLike.getFeed(), feedImageRepository.findAllByFeed(feedLike.getFeed()), true));
        }
        return feedResponseList;
    }
}
