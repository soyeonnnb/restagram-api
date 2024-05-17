package com.restgram.domain.user.service;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.user.dto.request.CustomerJoinRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.AddressErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import com.restgram.global.jwt.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse getUserInfo(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        LoginResponse res = LoginResponse.builder()
                .email(customer.getEmail())
                .nickname(customer.getNickname())
                .type(customer.getType())
                .build();
        return res;
    }

    @Override
    @Transactional
    public void join(CustomerJoinRequest req) {
        // 중복 이메일 확인
        if (customerRepository.existsByEmail(req.getEmail())) throw new RestApiException(UserErrorCode.EMAIL_DUPLICATED);

        // 중복 닉네임 확인
        if (userRepository.existsByNickname(req.getNickname())) throw new RestApiException(UserErrorCode.NICKNAME_DUPLICATED);

        // 비밀번호 암호화
        req.setPassword(passwordEncoder.encode(req.getPassword()));

        // 저장
        customerRepository.save(req.of());
    }
}
