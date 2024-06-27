package com.restgram.domain.coupon.service.impl;

import com.restgram.domain.coupon.dto.response.IssueCouponResponse;
import com.restgram.domain.coupon.entity.IssueCoupon;
import com.restgram.domain.coupon.repository.IssueCouponRepository;
import com.restgram.domain.coupon.service.IssueCouponService;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CouponErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueCouponServiceImpl implements IssueCouponService {

    private final CustomerRepository customerRepository;
    private final IssueCouponRepository issueCouponRepository;

    // 사용가능한 발급완료쿠폰
    @Override
    @Transactional(readOnly = true)
    public List<IssueCouponResponse> getCustomerCouponList(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID, "[일반] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + customerId + "]"));
        // 로그인 회원의 사용가능한 쿠폰 리스트
        List<IssueCoupon> issueCouponList = issueCouponRepository.findAllByCustomerAndIsUsedAndExpiredAtAfter(customer, false, LocalDateTime.now());

        return issueCouponList.stream()
                .map(IssueCouponResponse::of)
                .collect(Collectors.toList());
    }

    // 쿠폰 사용하기
    @Override
    @Transactional
    public void useCoupon(Long customerId, Long couponId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID, "[일반] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + customerId + "]"));
        IssueCoupon coupon = issueCouponRepository.findById(couponId).orElseThrow(() -> new RestApiException(CouponErrorCode.INVALID_COUPON_ISSUE_ID, "발급된 쿠폰ID가 유효하지 않습니다. [발급쿠폰ID=" + couponId + "]"));

        // 로그인 유저와 쿠폰 발급자가 일치하는지 확인
        if (coupon.getCustomer().getId() != customerId)
            throw new RestApiException(CouponErrorCode.INVALID_COUPON_USER, "쿠폰 소유자와 로그인 유저가 일치하지 않습니다. [로그안 사용자ID=" + customerId + ", 발급쿠폰ID=" + couponId + "]");

        // 사용된건지 확인
        if (coupon.getIsUsed())
            throw new RestApiException(CouponErrorCode.ALREADY_USED_COUPON, "이미 사용된 쿠폰입니다. [발급쿠폰ID=" + couponId + "]");

        // 만료 된건지 확인
        if (coupon.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new RestApiException(CouponErrorCode.EXPIRED_COUPON, "이미 만료된 쿠폰입니다. [발급쿠폰ID=" + couponId + "]");

        // 쿠폰 사용
        coupon.use();
    }
}
