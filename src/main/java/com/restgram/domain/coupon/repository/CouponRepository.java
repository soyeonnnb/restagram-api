package com.restgram.domain.coupon.repository;

import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.user.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findAllByStoreAndDisableAndFinishAtAfterOrderByStartAt(Store store, Boolean disable, LocalDateTime finishedAt);
    List<Coupon> findAllByStoreAndDisableOrFinishAtBeforeOrderByStartAt(Store store, Boolean disable, LocalDateTime finishedAt);
}
