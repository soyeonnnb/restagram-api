package com.restgram.domain.user.service;

import com.restgram.domain.user.dto.request.LoginRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.entity.LoginMethod;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.entity.UserType;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.domain.user.repository.RefreshTokenRepository;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.JwtTokenErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import com.restgram.global.jwt.token.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
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

}
