package com.restgram.domain.feed.service.impl;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.address.repository.EmdAddressRepository;
import com.restgram.domain.address.repository.SidoAddressRepository;
import com.restgram.domain.address.repository.SiggAddressRepository;
import com.restgram.domain.feed.dto.request.AddFeedRequest;
import com.restgram.domain.feed.dto.request.UpdateFeedRequest;
import com.restgram.domain.feed.dto.response.FeedResponse;
import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.feed.entity.FeedImage;
import com.restgram.domain.feed.repository.FeedImageRepository;
import com.restgram.domain.feed.repository.FeedLikeRepository;
import com.restgram.domain.feed.repository.FeedRepository;
import com.restgram.domain.feed.service.FeedImageService;
import com.restgram.domain.feed.service.FeedService;
import com.restgram.domain.follow.repository.FollowRepository;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.entity.PaginationResponse;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.AddressErrorCode;
import com.restgram.global.exception.errorCode.FeedErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedImageService feedImageService;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final FeedRepository feedRepository;
    private final FeedImageRepository feedImageRepository;
    private final FollowRepository followRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final EmdAddressRepository emdAddressRepository;
    private final SiggAddressRepository siggAddressRepository;
    private final SidoAddressRepository sidoAddressRepository;

    @Override
    @Transactional
    public void addFeed(Long userId, AddFeedRequest req, List<MultipartFile> images) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));

        Store store = storeRepository.findById(req.storeId())
                .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID,
                        "사용자ID가 유효하지 않습니다. [사용자ID=" + req.storeId() + "]"));

        // 피드 생성
        Feed feed = req.toEntity(user, store);
        feedRepository.save(feed);

        // 피드 별로 이미지 데이터 생성
        for (int idx = 0; idx < images.size(); idx++) {
            feedImageService.saveFeedImage(feed, idx, images.get(idx));
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<FeedResponse> getFeeds(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));

        // 팔로우 리스트 가져오기
        List<User> followUserList = followRepository.findFollowingsByFollower(user);
        followUserList.add(user);

        // 내 + 팔로우한 사람들의 피드 리스트 가져오기
        Page<Feed> feedList = feedRepository.findAllByWriterInOrderByIdDesc(followUserList, pageable);

        return feedList.stream().map(feed -> FeedResponse.of(feed, feed.getFeedImageList(),
                feedLikeRepository.existsByFeedAndUser(feed, user))).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse searchFeeds(Long userId, Long addressId, Integer addressRange,
                                          String query, Long cursorId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));


        List<EmdAddress> emdAddressList = new ArrayList<>();
        switch (addressRange) {
            case 1 -> emdAddressList = emdAddressRepository.findAllBySidoAddress(
                    sidoAddressRepository.findById(addressId)
                            .orElseThrow(() -> new RestApiException(AddressErrorCode.INVALID_SIDO_ID,
                                    "시도 ID가 유효하지 않습니다. [ID=" + addressId + "]")));
            case 2 -> emdAddressList = emdAddressRepository.findAllBySiggAddress(
                    siggAddressRepository.findById(addressId)
                            .orElseThrow(() -> new RestApiException(AddressErrorCode.INVALID_SIGG_ID,
                                    "시군구 ID가 유효하지 않습니다. [ID=" + addressId + "]")));
            case 3 -> emdAddressList.add(emdAddressRepository.findById(addressId)
                    .orElseThrow(() -> new RestApiException(AddressErrorCode.INVALID_EMD_ID,
                            "읍면동 ID가 유효하지 않습니다. [ID=" + addressId + "]")));
        }

        List<FeedResponse> feedResponseList = feedRepository.searchByQueryAndEmdAddressList(query, emdAddressList, cursorId, user);

        // 다음 커서 값 설정
        Long nextCursorId =
                !feedResponseList.isEmpty() ? feedResponseList.get(feedResponseList.size() - 1).id() : null;
        boolean hasNext = feedResponseList.size() == 20;  // 페이지 크기와 동일한 경우 다음 페이지가 있다고 간주

        return PaginationResponse.builder()
                .cursorId(nextCursorId)
                .hasNext(hasNext)
                .list(feedResponseList)
                .build();
    }

    @Override
    @Transactional
    public void deleteFeed(Long userId, Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RestApiException(FeedErrorCode.INVALID_FEED_ID,
                        "피드ID가 유효하지 않습니다. [피드ID=" + feedId + "]"));

        if (feed.getWriter().getId() != userId) {
            throw new RestApiException(UserErrorCode.USER_MISMATCH,
                    "로그인 사용자와 피드 작성자가 일치하지 않습니다. [로그인 사용자ID=" + userId + ", 피드ID=" + feedId + "]");
        }

        List<FeedImage> feedImageList = feedImageRepository.findAllByFeedOrderByNumber(feed);
        for (FeedImage feedImage : feedImageList) {
            feedImageRepository.delete(feedImage);
        }

        feedRepository.delete(feed);
    }

    @Override
    @Transactional
    public void updateFeed(Long userId, UpdateFeedRequest request) {
        Feed feed = feedRepository.findById(request.feedId())
                .orElseThrow(() -> new RestApiException(FeedErrorCode.INVALID_FEED_ID,
                        "피드ID가 유효하지 않습니다. [피드ID=" + request.feedId() + "]"));
        if (feed.getWriter().getId() != userId) {
            throw new RestApiException(UserErrorCode.USER_MISMATCH,
                    "로그인 사용자와 피드 작성자가 일치하지 않습니다. [로그인 사용자ID=" + userId + ", 피드ID=" + request.feedId()
                            + "]");
        }
        feed.updateContent(request.content());
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse getFeedsCursor(Long userId, Long cursorId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));
        // 팔로우 리스트 가져오기
        List<User> searchUserList = followRepository.findFollowingsByFollower(user);

        searchUserList.add(user);

        // 내 + 팔로우한 사람들의 피드 리스트 가져오기
        List<FeedResponse> feedResponseList = feedRepository.findByIdLessThanAndWriterInOrderByIdDescQuerydsl(cursorId,
                searchUserList, user);

        // 다음 커서 값 설정
        Long nextCursorId =
                !feedResponseList.isEmpty() ? feedResponseList.get(feedResponseList.size() - 1).id() : null;
        boolean hasNext = feedResponseList.size() == 20;  // 페이지 크기와 동일한 경우 다음 페이지가 있다고 간주

        return PaginationResponse.builder()
                .cursorId(nextCursorId)
                .hasNext(hasNext)
                .list(feedResponseList)
                .build();
    }

    @Override
    public PaginationResponse searchUserFeed(Long loginUserId, Long userId, Long cursorId) {
        User loginUser = userRepository.findById(loginUserId).orElseThrow(
                () -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(UserErrorCode.INVALID_USER_ID,
                        "사용자ID가 유효하지 않습니다. [사용자ID=" + userId + "]"));

        // 내 + 팔로우한 사람들의 피드 리스트 가져오기
        List<FeedResponse> feedResponseList = feedRepository.findByWriterAndIdLessOrGreaterThanIdDesc(loginUser, user, cursorId);

        // 다음 커서 값 설정

        Long nextCursorId = !feedResponseList.isEmpty() ? feedResponseList.get(feedResponseList.size() - 1).id() : null;

        boolean hasNext = feedResponseList.size() == 20;  // 페이지 크기와 동일한 경우 다음 페이지가 있다고 간주

        return PaginationResponse.builder()
                .cursorId(nextCursorId)
                .hasNext(hasNext)
                .list(feedResponseList)
                .build();
    }

}
