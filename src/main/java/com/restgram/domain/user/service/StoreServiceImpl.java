package com.restgram.domain.user.service;


import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.address.repository.EmdAddressRepository;
import com.restgram.domain.user.dto.request.JoinRequest;
import com.restgram.domain.user.dto.request.LoginRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.UserType;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.*;
import com.restgram.global.jwt.token.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final EmdAddressRepository emdAddressRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    // 회원가입
    @Override
    public void join(JoinRequest req) {
        // 중복 이메일 확인
        if (storeRepository.existsByEmail(req.getEmail())) throw new RestApiException(UserErrorCode.EMAIL_DUPLICATED);
        
        // 중복 닉네임 확인
        if (userRepository.existsByNickname(req.getNickname())) throw new RestApiException(UserErrorCode.NICKNAME_DUPLICATED);

        // 비밀번호 암호화
        req.setPassword(passwordEncoder.encode(req.getPassword()));

        // 주소 가져오기
        EmdAddress emdAddress = emdAddressRepository.findById(req.getBcode()).orElseThrow(() -> new RestApiException(AddressErrorCode.INVALID_BCODE));

        // 저장
        storeRepository.save(req.of(emdAddress));
    }

    // 로그인
    @Override
    @Transactional
    public LoginResponse login(LoginRequest req, HttpServletResponse response) {
        Store store = storeRepository.findByEmail(req.getEmail()).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        // 비밀번호 확인
        if (!passwordEncoder.matches(req.getPassword(), store.getPassword())) throw new RestApiException(UserErrorCode.PASSWORD_MISMATCH);
        tokenProvider.createTokens(store.getId(), UserType.STORE.getName(), response);

        LoginResponse res = LoginResponse.builder()
                .email(store.getEmail())
                .nickname(store.getNickname())
                .type(UserType.STORE.getName())
                .build();

        return res;
    }

}
