package com.restgram.domain.feed.service;

import com.restgram.domain.feed.dto.request.AddFeedRequest;
import com.restgram.domain.feed.dto.response.FeedResponse;
import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.feed.entity.FeedImage;
import com.restgram.domain.feed.repository.FeedImageRepository;
import com.restgram.domain.feed.repository.FeedLikeRepository;
import com.restgram.domain.feed.repository.FeedRepository;
import com.restgram.domain.follow.entity.Follow;
import com.restgram.domain.follow.repository.FollowRepository;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.S3ErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import com.restgram.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final S3Service s3Service;
    private final FeedRepository feedRepository;
    private final FeedImageRepository feedImageRepository;
    private final FollowRepository followRepository;
    private final FeedLikeRepository feedLikeRepository;

    @Override
    @Transactional
    public void addFeed(Long userId, AddFeedRequest req, List<MultipartFile> images) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        Store store = storeRepository.findById(req.getStoreId()).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        // 피드 생성
        Feed feed = Feed.builder()
                .writer(user)
                .store(store)
                .content(req.getContent())
                .build();
        feedRepository.save(feed);
        // 피드 별로 이미지 데이터 생성
        for(int idx=0;idx<images.size();idx++) {
            FeedImage feedImage = FeedImage.builder()
                    .number(idx)
                    // S3에 저장한다.
                    .url(s3Service.uploadFile(images.get(idx), "feed/"+feed.getId()))
                    .feed(feed)
                    .build();
            feedImageRepository.save(feedImage);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedResponse> getFeeds(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        // 팔로우 리스트 가져오기
        List<User> followUserList = followRepository.findFollowingsByFollower(user);
        // 내 + 팔로우한 사람들의 피드 리스트 가져오기
        List<Feed> feedList = feedRepository.findAllByWriterInOrWriterOrderByCreatedAtDesc(followUserList, user);
        // 응답 만들기
        List<FeedResponse> feedResponseList = new ArrayList<>();
        for(Feed feed : feedList) {
            feedResponseList.add(FeedResponse.of(feed, feedImageRepository.findAllByFeed(feed), feedLikeRepository.existsByFeedAndUser(feed, user)));
        }
        return feedResponseList;
    }
}
