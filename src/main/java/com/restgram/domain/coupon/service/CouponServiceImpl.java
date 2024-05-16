package com.restgram.domain.coupon.service;

import com.restgram.domain.coupon.dto.request.AddCouponReq;
import com.restgram.domain.coupon.dto.response.StoreCouponRes;
import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.coupon.repository.CouponRepository;
import com.restgram.domain.coupon.repository.IssueCouponRepository;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CommonErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{

    private final StoreRepository storeRepository;
    private final CouponRepository couponRepository;
    private final IssueCouponRepository issueCouponRepository;

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

    // 현재 가능한 쿠폰 리스트
    @Override
    public List<StoreCouponRes> getAvailableCouponList(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        // 취소처리X 하면서 종료일이 현재 이전인 쿠폰들
        List<Coupon> couponList = couponRepository.findAllByStoreAndDisableAndFinishAtAfterOrderByStartAt(store, false, LocalDateTime.now());
        List<StoreCouponRes> storeCouponResList = new ArrayList<>();
        for(Coupon coupon : couponList) {
            storeCouponResList.add(StoreCouponRes.of(coupon, issueCouponRepository.countAllByCouponAndIsUsed(coupon, true)));
        }
        return storeCouponResList;
    }

    @Override
    public List<StoreCouponRes> getFinsihCouponList(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        // 취소처리O 거나 종료일이 현재 이후인 쿠폰들
        List<Coupon> couponList = couponRepository.findAllByStoreAndDisableOrFinishAtBeforeOrderByStartAt(store, true, LocalDateTime.now());
        List<StoreCouponRes> storeCouponResList = new ArrayList<>();
        for(Coupon coupon : couponList) {
            storeCouponResList.add(StoreCouponRes.of(coupon, issueCouponRepository.countAllByCouponAndIsUsed(coupon, true)));
        }
        return storeCouponResList;
    }
}
