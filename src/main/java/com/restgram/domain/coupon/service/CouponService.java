package com.restgram.domain.coupon.service;

import com.restgram.domain.coupon.dto.request.AddCouponRequest;
import com.restgram.domain.coupon.dto.response.CustomerCouponResponse;
import com.restgram.domain.coupon.dto.response.StoreCouponResponse;
import com.restgram.global.entity.PaginationResponse;

import java.util.List;

public interface CouponService {
    void addCoupon(Long storeId, AddCouponRequest req);

    void stopCoupon(Long storeId, Long couponId);

    List<StoreCouponResponse> getAvailableCouponList(Long storeId);

    PaginationResponse<?> getFinishCouponList(Long storeId, Long cursorId);

    List<CustomerCouponResponse> getStoresCouponList(Long customerId, Long storeId);

    void issueCoupon(Long customerId, Long couponId);
}
