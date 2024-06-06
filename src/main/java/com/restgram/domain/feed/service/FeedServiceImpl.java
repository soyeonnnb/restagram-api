package com.restgram.domain.feed.service;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.address.repository.EmdAddressRepository;
import com.restgram.domain.address.repository.SidoAddressRepository;
import com.restgram.domain.address.repository.SiggAddressRepository;
import com.restgram.domain.feed.dto.request.AddFeedRequest;
import com.restgram.domain.feed.dto.request.UpdateFeedRequest;
import com.restgram.domain.feed.dto.response.FeedCursorResponse;
import com.restgram.domain.feed.dto.response.FeedResponse;
import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.feed.entity.FeedImage;
import com.restgram.domain.feed.repository.FeedImageRepository;
import com.restgram.domain.feed.repository.FeedLikeRepository;
import com.restgram.domain.feed.repository.FeedRepository;
import com.restgram.domain.follow.repository.FollowRepository;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CommonErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import com.restgram.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedImageService feedImageService;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final S3Service s3Service;
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
            feedImageService.saveFeedImage(feed, idx, images.get(idx));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedResponse> getFeeds(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        // 팔로우 리스트 가져오기
        List<User> followUserList = followRepository.findFollowingsByFollower(user);
        followUserList.add(user);
        // 내 + 팔로우한 사람들의 피드 리스트 가져오기
        Page<Feed> feedList = feedRepository.findAllByWriterInOrderByIdDesc(followUserList, pageable);
        // 응답 만들기
        List<FeedResponse> feedResponseList = new ArrayList<>();
        for(Feed feed : feedList) {
            feedResponseList.add(FeedResponse.of(feed, feed.getFeedImageList(), feedLikeRepository.existsByFeedAndUser(feed, user)));
        }
        return feedResponseList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedResponse> searchFeeds(Long userId, Long addressId, Integer addressRange, String query, Pageable pageable) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        List<EmdAddress> emdAddressList = new ArrayList<>();
        List<Feed> feedList;
        switch (addressRange) {
            case 1:
                emdAddressList = emdAddressRepository.findAllBySidoAddress(sidoAddressRepository.findById(addressId).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND)));
                break;
            case 2:
                emdAddressList = emdAddressRepository.findAllBySiggAddress(siggAddressRepository.findById(addressId).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND)));
                break;
            case 3:
                emdAddressList.add(emdAddressRepository.findById(addressId).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND)));
                break;
        }
        if (addressRange == 0) feedList = feedRepository.searchByQuery(query, pageable);
        else feedList = feedRepository.searchByQueryAndEmdAddressList(query, emdAddressList, pageable);

        List<FeedResponse> feedResponseList = new ArrayList<>();

        for(Feed feed : feedList) {
            feedResponseList.add(FeedResponse.of(feed, feedImageRepository.findAllByFeed(feed), feedLikeRepository.existsByFeedAndUser(feed, customer)));
        }

        return feedResponseList;
    }

    @Override
    @Transactional
    public void deleteFeed(Long userId, Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND));
        if (feed.getWriter().getId() != userId) throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
        List<FeedImage> feedImageList = feedImageRepository.findAllByFeed(feed);
        for(FeedImage feedImage : feedImageList) {
            System.out.println(s3Service.delete(feedImage.getUrl()));
            feedImageRepository.delete(feedImage);
        }
        feedRepository.delete(feed);
    }

    @Override
    @Transactional
    public void updateFeed(Long userId, UpdateFeedRequest request) {
        Feed feed = feedRepository.findById(request.getFeedId()).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND));
        if (feed.getWriter().getId() != userId) throw new RestApiException(UserErrorCode.USER_MISMATCH);
        feed.updateContent(request.getContent());
        feedRepository.save(feed);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedCursorResponse getFeedsCursor(Long userId, Long cursorId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        // 팔로우 리스트 가져오기
        List<User> followUserList = followRepository.findFollowingsByFollower(user);
        if (cursorId == null) {
            cursorId = feedRepository.findTopByOrderByIdDesc().orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND)).getId();
        }
        // 내 + 팔로우한 사람들의 피드 리스트 가져오기
        List<Feed> feedList = feedRepository.findTop20ByWriterInAndIdLessThanOrderByIdDesc(followUserList, cursorId);
        // 응답 생성
        List<FeedResponse> feedResponseList = feedList.stream()
                .map(feed -> FeedResponse.of(feed, feed.getFeedImageList(), feedLikeRepository.existsByFeedAndUser(feed, user)))
                .collect(Collectors.toList());


        // 다음 커서 값 설정
        Long nextCursorId = !feedList.isEmpty() ? feedList.get(feedList.size() - 1).getId() : null;
        boolean hasNext = feedList.size() == 20;  // 페이지 크기와 동일한 경우 다음 페이지가 있다고 간주

        FeedCursorResponse response = FeedCursorResponse.builder()
                .cursorId(nextCursorId)
                .hasNext(hasNext)
                .feeds(feedResponseList)
                .build();
        return response;
    }


}
