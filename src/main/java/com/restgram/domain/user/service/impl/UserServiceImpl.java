package com.restgram.domain.user.service.impl;

import com.restgram.domain.coupon.repository.CouponRepository;
import com.restgram.domain.feed.repository.FeedRepository;
import com.restgram.domain.follow.repository.FollowRepository;
import com.restgram.domain.user.dto.request.NicknameRequest;
import com.restgram.domain.user.dto.request.UpdatePasswordRequest;
import com.restgram.domain.user.dto.response.*;
import com.restgram.domain.user.entity.*;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public List<UserInfoResponse> searchUser(String query) {
        List<User> userList = userRepository.findAllByNicknameOrName(query);
        List<UserInfoResponse> userInfoResponseList = new ArrayList<>();
        for(User user : userList) {
            userInfoResponseList.add(UserInfoResponse.of(user));
        }
        return userInfoResponseList;
    }

    @Override
    public FeedUserInfoResponse getFeedUser(Long myId, Long userId) {
        User me = userRepository.findById(myId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        if (user.getType().equals(UserType.STORE.toString())) {
            return getFeedStore(me, userId);
        } else {
            return getFeedCustomer(me, userId);
        }
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        if (user.getType().toString().equals(UserType.CUSTOMER)) {
            Customer c = (Customer) user;
            // 소셜 로그인의 경우에는 비밀번호가 없음
            if (c.getLoginMethod().toString().equals(LoginMethod.KAKAO)) throw new RestApiException(UserErrorCode.USER_MISMATCH);
        }
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) throw new RestApiException(UserErrorCode.PASSWORD_MISMATCH);
        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateNickname(Long userId, NicknameRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        if (userRepository.existsByNickname(request.getNickname())) throw new RestApiException(UserErrorCode.NICKNAME_DUPLICATED);
        user.updateNickname(request.getNickname());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfileImage(Long userId, MultipartFile image) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        String url = null;
        if (image != null) {
            url = s3Service.uploadFile(image, "user/profile/" + user.getId());
        }
        if (user.getProfileImage() != null) {
            s3Service.delete(user.getProfileImage());
        }
        user.updateProfileImage(url);
        userRepository.save(user);
        return UserProfileResponse.builder().imageUrl(url).build();
    }


    @Override
    @Transactional(readOnly = true)
    public CheckResponse duplicateNickname(String nickname) {
        return CheckResponse.builder()
                .check(userRepository.existsByNickname(nickname))
                .build();
    }


    private FeedStoreInfoResponse getFeedStore(User me, Long userId) {
        Store store = storeRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        Integer feedNum = feedRepository.countAllByWriter(store);
        Integer followingNum = followRepository.countAllByFollowing(store);
        Integer reviewNum = feedRepository.countAllByStore(store);
        Integer couponNum = couponRepository.countAllByStoreAndDisableAndStartAtBeforeAndFinishAtAfter(store, false, LocalDateTime.now(), LocalDateTime.now());
        boolean isFollow = followRepository.existsByFollowerAndFollowing(me, store);
        FeedStoreInfoResponse response = FeedStoreInfoResponse.of(store, feedNum, followingNum, reviewNum, couponNum, isFollow);
        return response;
    }

    private FeedCustomerInfoResponse getFeedCustomer(User me, Long userId) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        Integer feedNum = feedRepository.countAllByWriter(customer);
        Integer followerNum = followRepository.countAllByFollower(customer);
        Integer followingNum = followRepository.countAllByFollowing(customer);
        boolean isFollow = followRepository.existsByFollowerAndFollowing(me, customer);
        FeedCustomerInfoResponse response = FeedCustomerInfoResponse.of(customer, feedNum, followerNum, followingNum, isFollow);
        return response;
    }


    @Override
    @Transactional
    public String reissue(HttpServletResponse response, String accessToken, String refreshToken) {
        // 토큰이 쿠키에 없으면 재로그인 요청
        if (accessToken == null || refreshToken == null) {
            tokenProvider.tokenCookieRemove(response, TYPE_ACCESS);
            tokenProvider.tokenCookieRemove(response, TYPE_REFRESH);
            throw new RestApiException(JwtTokenErrorCode.DOES_NOT_EXIST_TOKEN);
        }
        // db에 있는 값인지 확인한 후, db에 없으면 유효하지 않다고 판단 -> 재로그인 요청
        if (!refreshTokenRepository.existsByAccessTokenAndRefreshToken(accessToken, refreshToken)) throw new RestApiException(JwtTokenErrorCode.INVALID_TOKEN);

        // refresh token 유효기간 확인
        if (!tokenProvider.checkExpiredToken(refreshToken)) throw new RestApiException(JwtTokenErrorCode.EXPIRED_TOKEN);
        User user = userRepository.findById(tokenProvider.getUserId(refreshToken, response)).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));

        // 존재한다면 우선 토큰 삭제
        tokenProvider.tokenRemove(response, accessToken, refreshToken);
        String[] tokens = tokenProvider.createTokens(user.getId(), user.getType(), response);
        return tokens[0];
    }
}