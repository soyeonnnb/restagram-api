package com.restgram.domain.user.service.impl;


import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.address.repository.EmdAddressRepository;
import com.restgram.domain.user.dto.request.StoreJoinRequest;
import com.restgram.domain.user.dto.request.LoginRequest;
import com.restgram.domain.user.dto.request.UpdateStoreRequest;
import com.restgram.domain.user.dto.response.CheckResponse;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.StoreInfoResponse;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.UserType;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.domain.user.service.StoreService;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.*;
import com.restgram.global.jwt.token.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public void join(StoreJoinRequest req) {
        // 중복 이메일 확인
        if (storeRepository.existsByEmail(req.getEmail())) throw new RestApiException(UserErrorCode.EMAIL_DUPLICATED);
        
        // 중복 닉네임 확인
        if (userRepository.existsByNickname(req.getNickname())) throw new RestApiException(UserErrorCode.NICKNAME_DUPLICATED);

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(req.getPassword());

        // 주소 가져오기
        EmdAddress emdAddress = emdAddressRepository.findById(req.getBcode()).orElseThrow(() -> new RestApiException(AddressErrorCode.INVALID_BCODE));

        // 저장
        storeRepository.save(req.of(emdAddress, encodedPassword));
    }

    @Override
    public List<StoreInfoResponse> searchByName(String parameter) {
        List<Store> storeList = storeRepository.findAllByStoreNameOrNicknameContaining(parameter);
        List<StoreInfoResponse> storeInfoResponseList = new ArrayList<>();
        for(Store store : storeList) {
            storeInfoResponseList.add(StoreInfoResponse.of(store));
        }
        return storeInfoResponseList;
    }

    @Override
    @Transactional
    public void updateStore(Long userId, UpdateStoreRequest request) {
        Store store = storeRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        store.updateDescription(request.getDescription());
        store.updatePhone(request.getPhone());
        store.updateStoreInfo(request, emdAddressRepository.findById(request.getBcode()).orElseThrow(() -> new RestApiException(AddressErrorCode.INVALID_BCODE)));
        storeRepository.save(store);
    }

    // 로그인
    @Override
    @Transactional
    public LoginResponse login(LoginRequest req, HttpServletResponse response) {
        Store store = storeRepository.findByEmail(req.getEmail()).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        // 비밀번호 확인
        if (!passwordEncoder.matches(req.getPassword(), store.getPassword())) throw new RestApiException(UserErrorCode.PASSWORD_MISMATCH);
        tokenProvider.createTokens(store.getId(), UserType.STORE.getName(), response);

        LoginResponse res = LoginResponse.of(store);
        return res;
    }


    @Override
    @Transactional(readOnly = true)
    public CheckResponse duplicateEmail(String email) {
        return CheckResponse.builder()
                .check(storeRepository.existsByEmail(email))
                .build();
    }
}
