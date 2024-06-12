package com.restgram.domain.user.service.impl;

import com.restgram.domain.coupon.repository.CouponRepository;
import com.restgram.domain.feed.repository.FeedRepository;
import com.restgram.domain.follow.repository.FollowRepository;
import com.restgram.domain.user.dto.request.NicknameRequest;
import com.restgram.domain.user.dto.response.CheckResponse;
import com.restgram.domain.user.dto.response.FeedCustomerInfoResponse;
import com.restgram.domain.user.dto.response.FeedStoreInfoResponse;
import com.restgram.domain.user.dto.response.FeedUserInfoResponse;
import com.restgram.domain.user.dto.response.UserInfoResponse;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.entity.UserType;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.domain.user.repository.RefreshTokenRepository;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.domain.user.service.UserService;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.JwtTokenErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import com.restgram.global.jwt.token.JwtTokenProvider;
import com.restgram.global.s3.service.S3Service;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final StoreRepository storeRepository;
  private final CustomerRepository customerRepository;
  private final FeedRepository feedRepository;
  private final FollowRepository followRepository;
  private final CouponRepository couponRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  private final S3Service s3Service;

  private final JwtTokenProvider tokenProvider;
  private final PasswordEncoder passwordEncoder;
  private static final String TYPE_ACCESS = "access";
  private static final String TYPE_REFRESH = "refresh";

  @Override
  @Transactional
  public void logout(HttpServletResponse response, String accessToken, String refreshToken) {
    tokenProvider.tokenRemove(response, accessToken, refreshToken);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserInfoResponse> searchUser(String query) {
    List<User> userList = userRepository.findAllByNicknameOrName(query);

    return userList.stream().map(UserInfoResponse::of).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public FeedUserInfoResponse getFeedUser(Long myId, Long userId) {
    User me = userRepository.findById(myId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
            "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID,
            "사용자ID가 유효하지 않습니다. [사용자ID=" + userId + "]"));

    // 유저 별로 피드 정보가 상이함
    if (user.getType().equals(UserType.STORE.toString())) {
      return getFeedStore(me, userId);
    } else {
      return getFeedCustomer(me, userId);
    }
  }

  @Override
  @Transactional
  public void updateNickname(Long userId, NicknameRequest request) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
            "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));
    if (userRepository.existsByNickname(request.nickname())) {
      throw new RestApiException(UserErrorCode.NICKNAME_DUPLICATED,
          "닉네임이 중복되었습니다. [닉네임=" + request.nickname() + "]");
    }

    user.updateNickname(request.nickname());
  }

  @Override
  @Transactional
  public void updateProfileImage(Long userId, MultipartFile image) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
            "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));
    if (image != null) {
      user.updateProfileImage(s3Service.uploadFile(image, "user/profile/" + user.getId()));
    }
    if (user.getProfileImage() != null) {
      s3Service.delete(user.getProfileImage());
    }
  }


  @Override
  @Transactional(readOnly = true)
  public CheckResponse duplicateNickname(String nickname) {
    return CheckResponse.builder()
        .check(userRepository.existsByNickname(nickname))
        .build();
  }


  private FeedStoreInfoResponse getFeedStore(User me, Long userId) {
    Store store = storeRepository.findById(userId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID,
            "[가게] 사용자ID가 유효하지 않습니다. [사용자ID=" + userId + "]"));

    Integer feedNum = feedRepository.countAllByWriter(store); // 피드 수
    Integer followingNum = followRepository.countAllByFollowing(store); // 팔로잉 수
    Integer reviewNum = feedRepository.countAllByStore(store); // 리뷰 수
    Integer couponNum = couponRepository.countAllByStoreAndDisableAndStartAtBeforeAndFinishAtAfter(
        store, false, LocalDateTime.now(), LocalDateTime.now()); // 발급 가능한 쿠폰 수
    boolean isFollow = followRepository.existsByFollowerAndFollowing(me, store); // 팔로우 중인지
    return FeedStoreInfoResponse.of(store, feedNum, followingNum,
        reviewNum, couponNum, isFollow);
  }

  private FeedCustomerInfoResponse getFeedCustomer(User me, Long userId) {
    Customer customer = customerRepository.findById(userId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID,
            "[일반] 사용자ID가 유효하지 않습니다. [사용자ID=" + userId + "]"));

    Integer feedNum = feedRepository.countAllByWriter(customer); // 피드 수
    Integer followerNum = followRepository.countAllByFollower(customer); // 팔로워 수
    Integer followingNum = followRepository.countAllByFollowing(customer); // 팔로잉 수
    boolean isFollow = followRepository.existsByFollowerAndFollowing(me, customer); // 팔로우 중인지
    return FeedCustomerInfoResponse.of(customer, feedNum, followerNum,
        followingNum, isFollow);
  }

  @Override
  @Transactional
  public void reissue(HttpServletResponse response, String accessToken, String refreshToken) {
    // 토큰이 쿠키에 없으면 재로그인 요청
    if (accessToken == null || refreshToken == null) {
      tokenProvider.tokenCookieRemove(response, TYPE_ACCESS);
      tokenProvider.tokenCookieRemove(response, TYPE_REFRESH);
      throw new RestApiException(JwtTokenErrorCode.DOES_NOT_EXIST_TOKEN);
    }
    // db에 있는 값인지 확인한 후, db에 없으면 유효하지 않다고 판단 -> 재로그인 요청
    if (!refreshTokenRepository.existsByAccessTokenAndRefreshToken(accessToken, refreshToken)) {
      throw new RestApiException(JwtTokenErrorCode.INVALID_TOKEN);
    }

    // refresh token 유효기간 확인
    if (!tokenProvider.checkExpiredToken(refreshToken)) {
      throw new RestApiException(JwtTokenErrorCode.EXPIRED_TOKEN);
    }
    User user = userRepository.findById(tokenProvider.getUserId(refreshToken, response))
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));

    // 존재한다면 우선 토큰 삭제
    tokenProvider.tokenRemove(response, accessToken, refreshToken);
    tokenProvider.createTokens(user.getId(), user.getType(), response);
  }
}
