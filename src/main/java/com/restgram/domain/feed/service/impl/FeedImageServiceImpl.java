package com.restgram.domain.feed.service.impl;

import com.restgram.domain.feed.dto.response.FeedCursorResponse;
import com.restgram.domain.feed.dto.response.FeedImageCursorResponse;
import com.restgram.domain.feed.dto.response.FeedResponse;
import com.restgram.domain.feed.dto.response.UserFeedImageResponse;
import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.feed.entity.FeedImage;
import com.restgram.domain.feed.repository.FeedImageRepository;
import com.restgram.domain.feed.repository.FeedRepository;
import com.restgram.domain.feed.service.FeedImageService;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CommonErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import com.restgram.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedImageServiceImpl implements FeedImageService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final S3Service s3Service;
    private final FeedRepository feedRepository;
    private final FeedImageRepository feedImageRepository;

    // 피드리스트에서 첫번째 피드 리스트만 가져오기
    @Override
    public FeedImageCursorResponse getFeedImageList(Long userId, Long cursorId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        if (cursorId == null) {
            cursorId = feedRepository.findTopByOrderByIdDesc().orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND)).getId();
        }
        List<FeedImage> feedImageList = feedImageRepository.findByFeedWriterAndNumberAndIdLessThanOrderByIdDesc(user, 0, cursorId, PageRequest.of(0, 20));
        List<UserFeedImageResponse> userFeedImageResponseList = feedImageList.stream()
                .map(feedImage -> UserFeedImageResponse.of(feedImage))
                .collect(Collectors.toList());


        // 다음 커서 값 설정
        Long nextCursorId = !feedImageList.isEmpty() ? userFeedImageResponseList.get(userFeedImageResponseList.size() - 1).getId() : null;
        boolean hasNext = userFeedImageResponseList.size() == 20;  // 페이지 크기와 동일한 경우 다음 페이지가 있다고 간주

        FeedImageCursorResponse response = FeedImageCursorResponse.builder()
                .cursorId(nextCursorId)
                .images(userFeedImageResponseList)
                .hasNext(hasNext)
                .build();
        return response;
    }

    @Override
    public FeedImageCursorResponse getReviewImageList(Long userId, Long cursorId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        if (cursorId == null) {
            cursorId = feedRepository.findTopByOrderByIdDesc().orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND)).getId();
        }
        List<FeedImage> feedImageList = feedImageRepository.findByFeedStoreAndNumberOrderByIdDesc(user, 0, cursorId, PageRequest.of(0, 20));
        List<UserFeedImageResponse> userFeedImageResponseList = feedImageList.stream()
                .map(feedImage -> UserFeedImageResponse.of(feedImage))
                .collect(Collectors.toList());


        // 다음 커서 값 설정
        Long nextCursorId = !feedImageList.isEmpty() ? userFeedImageResponseList.get(userFeedImageResponseList.size() - 1).getId() : null;
        boolean hasNext = userFeedImageResponseList.size() == 20;  // 페이지 크기와 동일한 경우 다음 페이지가 있다고 간주

        FeedImageCursorResponse response = FeedImageCursorResponse.builder()
                .cursorId(nextCursorId)
                .images(userFeedImageResponseList)
                .hasNext(hasNext)
                .build();
        return response;
    }

    @Async("s3AsyncExecutor")
    @Override
    @Transactional
    public void saveFeedImage(Feed feed, Integer idx, MultipartFile file) {
        FeedImage feedImage = FeedImage.builder()
                .number(idx)
                // S3에 저장한다.
                .url(s3Service.uploadFile(file, "feed/"+feed.getId()))
                .feed(feed)
                .build();
        feedImageRepository.save(feedImage);
    }
}
