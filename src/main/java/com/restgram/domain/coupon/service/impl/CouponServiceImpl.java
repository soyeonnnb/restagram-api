package com.restgram.domain.coupon.service.impl;

import com.restgram.domain.coupon.dto.request.AddCouponRequest;
import com.restgram.domain.coupon.dto.response.CustomerCouponResponse;
import com.restgram.domain.coupon.dto.response.StoreCouponResponse;
import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.coupon.entity.IssueCoupon;
import com.restgram.domain.coupon.repository.CouponRepository;
import com.restgram.domain.coupon.repository.IssueCouponRepository;
import com.restgram.domain.coupon.service.CouponService;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CouponErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final CouponRepository couponRepository;
    private final IssueCouponRepository issueCouponRepository;

    @Value("${server.host.api}")
    private String serverUrl;

    @Override
    @Transactional
    public void addCoupon(Long storeId, AddCouponRequest req) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID, "[가게] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID="+storeId+"]"));
        // 시작일은 종료일 이전이여야 한다.
        if (req.getStartAt().isAfter(req.getFinishAt())) throw new RestApiException(CouponErrorCode.INVALID_COUPON_START_AT, "쿠폰 시작일은 종료일 이전이여야 합니다. [시작일="+req.getStartAt()+", 종료일="+req.getFinishAt()+"]");

        // 저장
        couponRepository.save(req.of(store));
    }

    @Override
    @Transactional
    public void stopCoupon(Long storeId, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new RestApiException(CouponErrorCode.INVALID_COUPON_ID, "쿠폰ID가 유효하지 않습니다. [쿠폰ID="+couponId+"]"));
        if (coupon.getStore().getId() != storeId) throw new RestApiException(UserErrorCode.USER_MISMATCH, "쿠폰 발급자와 로그인 유저가 일치하지 않습니다. [로그인 사용자ID="+storeId+", 쿠폰 발급자ID="+coupon.getStore().getId()+"]");

        // 불가능 체크
        coupon.setDisable(true);
    }

    // 현재 가능한 쿠폰 리스트
    @Override
    @Transactional(readOnly = true)
    public List<StoreCouponResponse> getAvailableCouponList(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID, "[가게] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID="+storeId+"]"));
        // 취소처리X 하면서 종료일이 현재 후전인 쿠폰들
        List<Coupon> couponList = couponRepository.findAllByStoreAndDisableAndFinishAtAfterOrderByStartAt(store, false, LocalDateTime.now());

        return couponList.stream()
                .map(coupon -> StoreCouponResponse.of(coupon, issueCouponRepository.countAllByCouponAndIsUsed(coupon, true)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreCouponResponse> getFinsihCouponList(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID, "[가게] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID="+storeId+"]"));
        // 취소처리O 거나 종료일이 현재 이전인 쿠폰들
        List<Coupon> couponList = couponRepository.findAllByStoreAndDisableOrFinishAtBeforeOrderByStartAt(store, true, LocalDateTime.now());

        return couponList.stream()
                .map(coupon -> StoreCouponResponse.of(coupon, issueCouponRepository.countAllByCouponAndIsUsed(coupon, true)))
                .collect(Collectors.toList());
    }

    // 가게 쿠폰 전체 가져오기
    @Override
    @Transactional(readOnly = true)
    public List<CustomerCouponResponse> getStoresCouponList(Long customerId, Long storeId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID, "[일반] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID="+customerId+"]"));
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID, "사용자ID가 유효하지 않습니다. [사용자ID="+storeId+"]"));
        // 취소처리X 하면서 시작일이 현재 이전이면서 종료일이 현재 이후인 쿠폰들
        List<Coupon> couponList = couponRepository.findAllByStoreAndDisableAndStartAtBeforeAndFinishAtAfterOrderByStartAt(store, true, LocalDateTime.now(), LocalDateTime.now());

        return couponList.stream()
                .map(coupon -> CustomerCouponResponse.of(coupon, issueCouponRepository.existsByCustomerAndCoupon(customer, coupon)))
                .collect(Collectors.toList());
    }

    // 쿠폰 발급
    @Override
    @Transactional
    public void issueCoupon(Long customerId, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new RestApiException(CouponErrorCode.INVALID_COUPON_ID, "쿠폰ID가 유효하지 않습니다. [쿠폰ID="+couponId+"]"));
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID, "[일반] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID="+customerId+"]"));

        // 발급 조건 확인
        // 1. 잔여 수량이 1 이상인지
        if (coupon.getRemainQuantity() == 0) throw new RestApiException(CouponErrorCode.REMAIN_QUANTITY_ZERO, "현재 쿠폰의 잔여수량이 없습니다. [쿠폰ID="+couponId+"]");
        // 2. 종료처리가 되어있는지
        else if (coupon.getDisable()) throw new RestApiException(CouponErrorCode.FINISH_COUPON_ISSUE, "현재 발급이 종료된 쿠폰입니다. [쿠폰ID="+couponId+"]"); // 발급 종료 쿠폰
        // 3. 발급 기간인지
        else if (coupon.getStartAt().isAfter(LocalDateTime.now()) || coupon.getFinishAt().isBefore(LocalDateTime.now())) throw new RestApiException(CouponErrorCode.NOT_ISSUE_PERIOD, "쿠폰 발급 기간이 아닙니다. [쿠폰ID"+couponId+", 발급 시작일="+coupon.getStartAt()+", 발급 종료일="+coupon.getFinishAt()+"]"); // 발급 종료 쿠폰
        // 4. 이전에 발급하지 않았는지
        else if (issueCouponRepository.existsByCustomerAndCoupon(customer, coupon)) throw new RestApiException(CouponErrorCode.ALREADY_ISSUED_COUPON, "이미 발급받은 쿠폰입니다. [로그인 사용자ID="+customerId+", 쿠폰ID="+couponId+"]");

        coupon.issueCoupon();
        IssueCoupon issueCoupon = IssueCoupon.builder()
                .customer(customer)
                .coupon(coupon)
                .isUsed(false)
                .expiredAt(LocalDateTime.now().plusMinutes(coupon.getExpiredMinute()))
                .build();

        issueCouponRepository.save(issueCoupon);
        issueCoupon.setQrImage(makeQrCode(issueCoupon));
    }

    // zxing를 이용한 QrImage 생성
    private String makeQrCode(IssueCoupon issueCoupon) {
        StringBuilder qrImage = new StringBuilder();
        qrImage.append(serverUrl);

        return qrImage.toString();

    }
}
