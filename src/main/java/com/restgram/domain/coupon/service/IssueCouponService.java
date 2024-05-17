package com.restgram.domain.coupon.service;

import com.restgram.domain.coupon.dto.response.CustomerCouponRes;
import com.restgram.domain.coupon.dto.response.IssueCouponRes;

import java.util.List;

public interface IssueCouponService {
    List<IssueCouponRes> getCustomerCouponList(Long customerId);
}
