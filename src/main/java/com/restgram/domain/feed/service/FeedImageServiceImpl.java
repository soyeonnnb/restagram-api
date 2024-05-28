package com.restgram.domain.feed.service;

import com.restgram.domain.feed.dto.response.UserFeedImageResponse;
import com.restgram.domain.feed.entity.FeedImage;
import com.restgram.domain.feed.repository.FeedImageRepository;
import com.restgram.domain.feed.repository.FeedRepository;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.UserErrorCode;
import com.restgram.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedImageServiceImpl implements FeedImageService{

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final S3Service s3Service;
    private final FeedRepository feedRepository;
    private final FeedImageRepository feedImageRepository;

    // 피드리스트에서 첫번째 피드 리스트만 가져오기
    @Override
    public List<UserFeedImageResponse> getFeedImageList(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        List<FeedImage> feedImageList = feedImageRepository.findByFeedWriterAndNumberOrderByIdDesc(user, 0, pageable);
        List<UserFeedImageResponse> userFeedImageResponseList = new ArrayList<>();
        for(FeedImage feedImage : feedImageList) {
            userFeedImageResponseList.add(UserFeedImageResponse.of(feedImage));
        }
        return userFeedImageResponseList;
    }

    @Override
    public List<UserFeedImageResponse> getReviewImageList(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        List<FeedImage> feedImageList = feedImageRepository.findByFeedStoreAndNumberOrderByIdDesc(user, 0, pageable);
        List<UserFeedImageResponse> userFeedImageResponseList = new ArrayList<>();
        for(FeedImage feedImage : feedImageList) {
            userFeedImageResponseList.add(UserFeedImageResponse.of(feedImage));
        }
        return userFeedImageResponseList;
    }
}
