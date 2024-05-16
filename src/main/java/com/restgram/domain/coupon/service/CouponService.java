package com.restgram.domain.coupon.service;

import com.restgram.domain.coupon.dto.request.AddCouponReq;

public interface CouponService {
    void addCoupon(Long id, AddCouponReq req);
    void stopCoupon(Long id, Long couponId);
}
