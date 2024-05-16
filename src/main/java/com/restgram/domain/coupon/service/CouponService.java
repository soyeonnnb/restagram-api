package com.restgram.domain.coupon.service;

import com.restgram.domain.coupon.dto.request.AddCouponReq;
import com.restgram.domain.coupon.dto.response.StoreCouponRes;

import java.util.List;

public interface CouponService {
    void addCoupon(Long id, AddCouponReq req);
    void stopCoupon(Long id, Long couponId);
    List<StoreCouponRes> getAvailableCouponList(Long id);
    List<StoreCouponRes> getFinsihCouponList(Long id);
}
