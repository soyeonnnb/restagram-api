package com.restgram.domain.user.service;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.restgram.domain.coupon.repository.CouponRepository;
import com.restgram.domain.feed.repository.FeedRepository;
import com.restgram.domain.follow.repository.FollowRepository;
import com.restgram.domain.user.dto.request.LoginRequest;
import com.restgram.domain.user.dto.request.UpdatePasswordRequest;
import com.restgram.domain.user.dto.response.*;
import com.restgram.domain.user.entity.*;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.domain.user.repository.RefreshTokenRepository;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CommonErrorCode;
import com.restgram.global.exception.errorCode.JwtTokenErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import com.restgram.global.jwt.token.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final FeedRepository feedRepository;
    private final FollowRepository followRepository;
    private final CouponRepository couponRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";


    // 로그인
    @Override
    @Transactional
    public LoginResponse login(LoginRequest req, HttpServletResponse response) {
        User user = null;
        if (req.getType().getName().equals(UserType.STORE.getName())) {
            user = (User) storeRepository.findByEmail(req.getEmail()).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        } else if (req.getType().getName().equals(UserType.CUSTOMER.getName())) {
            user = (User) customerRepository.findByEmailAndLoginMethod(req.getEmail(), LoginMethod.DEFAULT).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        }
        if (user == null) throw new RestApiException(UserErrorCode.USER_NOT_FOUND);
        // 비밀번호 확인
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) throw new RestApiException(UserErrorCode.PASSWORD_MISMATCH);
        tokenProvider.createTokens(user.getId(), user.getType(), response);
        LoginResponse res = LoginResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .type(user.getType())
                .build();

        return res;
    }


    @Override
    @Transactional
    public void logout(HttpServletResponse response, String accessToken, String refreshToken) {
        tokenProvider.tokenRemove(response, accessToken, refreshToken);
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
        if (!refreshTokenRepository.existsByAccessTokenAndRefreshToken(accessToken, refreshToken)) throw new RestApiException(JwtTokenErrorCode.INVALID_TOKEN);
        User user = userRepository.findById(tokenProvider.getUserId(refreshToken, response)).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        // 존재한다면 우선 토큰 삭제
        tokenProvider.tokenRemove(response, accessToken, refreshToken);
        tokenProvider.createTokens(user.getId(), user.getType(), response);
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
        User me = userRepository.findById(myId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        System.out.println(user.getType());
        if (user.getType().equals(UserType.STORE.toString())) {
            System.out.println("zz");
            return getFeedStore(me, userId);
        } else {
            return getFeedCustomer(me, userId);
        }
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        if (user.getType().toString().equals(UserType.CUSTOMER)) {
            Customer c = (Customer) user;
            // 소셜 로그인의 경우에는 비밀번호가 없음
            if (c.getLoginMethod().toString().equals(LoginMethod.KAKAO)) throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
        }
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) throw new RestApiException(UserErrorCode.PASSWORD_MISMATCH);
        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private FeedStoreInfoResponse getFeedStore(User me, Long userId) {
        Store store = storeRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        Integer feedNum = feedRepository.countAllByWriter(store);
        Integer followingNum = followRepository.countAllByFollowing(store);
        Integer reviewNum = feedRepository.countAllByStore(store);
        Integer couponNum = couponRepository.countAllByStoreAndDisableAndStartAtBeforeAndFinishAtAfter(store, false, LocalDateTime.now(), LocalDateTime.now());
        boolean isFollow = followRepository.existsByFollowerAndFollowing(me, store);
        FeedStoreInfoResponse response = FeedStoreInfoResponse.of(store, feedNum, followingNum, reviewNum, couponNum, isFollow);
        return response;
    }

    private FeedCustomerInfoResponse getFeedCustomer(User me, Long userId) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        Integer feedNum = feedRepository.countAllByWriter(customer);
        Integer followerNum = followRepository.countAllByFollower(customer);
        Integer followingNum = followRepository.countAllByFollowing(customer);
        boolean isFollow = followRepository.existsByFollowerAndFollowing(me, customer);
        FeedCustomerInfoResponse response = FeedCustomerInfoResponse.of(customer, feedNum, followerNum, followingNum, isFollow);
        return response;
    }

}
