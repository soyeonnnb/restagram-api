package com.restgram.domain.coupon.repository;

import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.coupon.entity.IssueCoupon;
import com.restgram.domain.user.entity.Customer;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IssueCouponRepository extends JpaRepository<IssueCoupon, Long> {
    Long countAllByCouponAndIsUsed(Coupon coupon, Boolean isUsed);
    boolean existsByCustomerAndCoupon(Customer customer,Coupon coupon);

    @EntityGraph(attributePaths = {"coupon", "coupon.store", "coupon.store.address", "coupon.store.address.siggAddress", "coupon.store.address.siggAddress.sidoAddress"})
    List<IssueCoupon> findAllByCustomerAndIsUsedAndExpiredAtAfter(Customer customer, Boolean isUsed, LocalDateTime expiredAt);
}
