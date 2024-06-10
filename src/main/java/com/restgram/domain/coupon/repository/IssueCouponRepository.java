package com.restgram.domain.coupon.repository;

import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.coupon.entity.IssueCoupon;
import com.restgram.domain.user.entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IssueCouponRepository extends JpaRepository<IssueCoupon, Long> {
    Long countAllByCouponAndIsUsed(Coupon coupon, Boolean isUsed);
    boolean existsByCustomerAndCoupon(Customer customer,Coupon coupon);

    @EntityGraph(attributePaths = {"coupon", "coupon.store", "coupon.store.emdAddress", "coupon.store.emdAddress.siggAddress", "coupon.store.emdAddress.siggAddress.sidoAddress"})
    List<IssueCoupon> findAllByCustomerAndIsUsedAndExpiredAtAfter(Customer customer, Boolean isUsed, LocalDateTime expiredAt);
}
