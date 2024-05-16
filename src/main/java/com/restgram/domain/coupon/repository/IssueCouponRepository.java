package com.restgram.domain.coupon.repository;

import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.coupon.entity.IssueCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueCouponRepository extends JpaRepository<IssueCoupon, Long> {
    Long countAllByCouponAndIsUsed(Coupon coupon, Boolean isUsed);
}
