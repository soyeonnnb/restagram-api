package com.restgram.domain.coupon.service;

import com.restgram.domain.coupon.dto.response.IssueCouponResponse;

import java.util.List;

public interface IssueCouponService {
    List<IssueCouponResponse> getCustomerCouponList(Long customerId);
}
