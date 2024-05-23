package com.restgram.domain.coupon.repository;

import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.user.entity.Store;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ) // 비관적 락 사용
    Optional<Coupon> findById(Long id);
    List<Coupon> findAllByStoreAndDisableAndFinishAtAfterOrderByStartAt(Store store, Boolean disable, LocalDateTime finishedAt);
    List<Coupon> findAllByStoreAndDisableOrFinishAtBeforeOrderByStartAt(Store store, Boolean disable, LocalDateTime finishedAt);
    List<Coupon> findAllByStoreAndDisableAndStartAtBeforeAndFinishAtAfterOrderByStartAt(Store store, Boolean disable, LocalDateTime startAt, LocalDateTime finishAt);
    Integer countAllByStoreAndDisableAndStartAtBeforeAndFinishAtAfter(Store store, Boolean disable, LocalDateTime startAt, LocalDateTime finishedAt);

    @EntityGraph(attributePaths = {"store"})
    List<Coupon> findAllByStartAtAndDisable(LocalDateTime dateTime, Boolean disable);

}
