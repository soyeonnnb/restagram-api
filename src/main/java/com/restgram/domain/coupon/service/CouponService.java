package com.restgram.domain.coupon.service;

import com.restgram.domain.coupon.dto.request.AddCouponReq;
import com.restgram.domain.coupon.dto.response.CustomerCouponRes;
import com.restgram.domain.coupon.dto.response.StoreCouponRes;

import java.util.List;

public interface CouponService {
    void addCoupon(Long storeId, AddCouponReq req);
    void stopCoupon(Long storeId, Long couponId);
    List<StoreCouponRes> getAvailableCouponList(Long storeId);
    List<StoreCouponRes> getFinsihCouponList(Long storeId);
    List<CustomerCouponRes> getStoresCouponList(Long customerId, Long storeId);
    void issueCoupon(Long customerId, Long couponId);
}
