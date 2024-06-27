package com.restgram.domain.coupon.repository;

import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.user.entity.Store;

import java.util.List;

public interface CouponRepositoryCustom {

    List<Coupon> findAllStoresNotFinishedCoupon(Store store);

    List<Coupon> findAllStoresFinishedCoupon(Store store, Long cursorId);

    // 발급 가능한 쿠폰
    List<Coupon> findAllStoresAvailableCoupon(Store store);

}
