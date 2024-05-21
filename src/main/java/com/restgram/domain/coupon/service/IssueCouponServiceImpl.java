package com.restgram.domain.coupon.service;

import com.restgram.domain.coupon.dto.response.IssueCouponResponse;
import com.restgram.domain.coupon.entity.IssueCoupon;
import com.restgram.domain.coupon.repository.IssueCouponRepository;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueCouponServiceImpl implements IssueCouponService{

    private final CustomerRepository customerRepository;
    private final IssueCouponRepository issueCouponRepository;

    // 사용가능한 발급완료쿠폰
    @Override
    public List<IssueCouponResponse> getCustomerCouponList(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        List<IssueCoupon> issueCouponList = issueCouponRepository.findAllByCustomerAndIsUsedAndExpiredAtAfter(customer, false, LocalDateTime.now());
        List<IssueCouponResponse> issueCouponResponseList = new ArrayList<>();
        for(IssueCoupon issueCoupon : issueCouponList) {
            issueCouponResponseList.add(IssueCouponResponse.of(issueCoupon));
        }
        return issueCouponResponseList;
    }
}
