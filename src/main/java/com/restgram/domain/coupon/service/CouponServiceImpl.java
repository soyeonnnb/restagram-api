package com.restgram.domain.coupon.service;

import com.restgram.domain.coupon.dto.request.AddCouponReq;
import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.coupon.repository.CouponRepository;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CommonErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{

    private final StoreRepository storeRepository;
    private final CouponRepository couponRepository;

    @Override
    public void addCoupon(Long id, AddCouponReq req) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        // 시작일은 종료일 이전이여야 한다.
        if (req.getStartAt().isAfter(req.getFinishAt())) throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);



        // 저장
        couponRepository.save(req.of(store));
    }

    @Override
    @Transactional
    public void stopCoupon(Long id, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND));
        if (coupon.getStore().getId() != id) throw new RestApiException(UserErrorCode.USER_MISMATCH);
        // 불가능 체크
        coupon.setDisable(true);
    }
}
