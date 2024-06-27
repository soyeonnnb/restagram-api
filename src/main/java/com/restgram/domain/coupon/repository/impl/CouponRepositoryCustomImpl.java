package com.restgram.domain.coupon.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.coupon.repository.CouponRepositoryCustom;
import com.restgram.domain.user.entity.Store;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.restgram.domain.coupon.entity.QCoupon.coupon;

@RequiredArgsConstructor
public class CouponRepositoryCustomImpl implements CouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 아직 종료 안된 쿠폰
    @Override
    public List<Coupon> findAllStoresNotFinishedCoupon(Store store) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(coupon.store.eq(store));
        builder.and(coupon.disable.isFalse());
        builder.and(coupon.finishAt.after(LocalDateTime.now()));

        List<Coupon> couponList = queryFactory
                .selectFrom(coupon)
                .where(builder)
                .limit(20)
                .orderBy(coupon.id.desc())
                .fetch();

        return couponList;
    }


    @Override
    public List<Coupon> findAllStoresFinishedCoupon(Store store, Long cursorId) {
        BooleanBuilder builder = new BooleanBuilder();
        if (cursorId != null) builder.and(coupon.id.lt(cursorId));
        builder.and(coupon.store.eq(store));
        builder.and(coupon.disable.isTrue().or(coupon.finishAt.before(LocalDateTime.now())));

        List<Coupon> couponList = queryFactory
                .selectFrom(coupon)
                .where(builder)
                .limit(20)
                .orderBy(coupon.id.desc())
                .fetch();

        return couponList;
    }

    @Override
    public List<Coupon> findAllStoresAvailableCoupon(Store store) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(coupon.store.eq(store));
        builder.and(coupon.disable.isFalse());
        builder.and(coupon.startAt.before(LocalDateTime.now()));
        builder.and(coupon.finishAt.after(LocalDateTime.now()));

        List<Coupon> couponList = queryFactory
                .selectFrom(coupon)
                .where(builder)
                .limit(20)
                .orderBy(coupon.discountMoney.desc())
                .fetch();

        return couponList;
    }
}
