package com.restgram.domain.user.service;

import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse getUserInfo(Long id) {
        User user = customerRepository.findById(id).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        LoginResponse res = LoginResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .type(user.getType())
                .build();
        return res;
    }
}
