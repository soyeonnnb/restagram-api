package com.restgram.domain.coupon.controller;

import com.restgram.domain.coupon.dto.request.AddCouponRequest;
import com.restgram.domain.coupon.dto.response.CustomerCouponResponse;
import com.restgram.domain.coupon.dto.response.StoreCouponResponse;
import com.restgram.domain.coupon.service.CouponService;
import com.restgram.global.exception.entity.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/coupon")
@Slf4j
public class CouponController {

    private final CouponService couponService;

    // 쿠폰 발급
    @PostMapping
    public CommonResponse registerCoupon(Authentication authentication, @RequestBody @Valid AddCouponRequest req) {
        Long storeId = Long.parseLong(authentication.getName());
        couponService.addCoupon(storeId, req);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("쿠폰 발급 완료")
                .build();
    }
    
    // 쿠폰 발급 종료
    @PostMapping("/stop/{couponId}")
    public CommonResponse stopIssueCoupon(Authentication authentication, @PathVariable Long couponId) {
        Long storeId = Long.parseLong(authentication.getName());
        couponService.stopCoupon(storeId, couponId);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("쿠폰 발급 취소 완료")
                .build();
    }

    @GetMapping
    public CommonResponse getAvailableCouponList(Authentication authentication) {
        Long storeId = Long.parseLong(authentication.getName());
        List<StoreCouponResponse> res = couponService.getAvailableCouponList(storeId);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .data(res)
                .message("쿠폰 리스트 가져오기 성공")
                .build();
    }

    @GetMapping("/finish")
    public CommonResponse getFinishCouponList(Authentication authentication) {
        Long storeId = Long.parseLong(authentication.getName());
        List<StoreCouponResponse> res = couponService.getFinsihCouponList(storeId);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .data(res)
                .message("쿠폰 리스트 가져오기 성공")
                .build();
    }



    // 구매자 -> 가게 발급가능 쿠폰 가져오기
    @GetMapping("/{storeId}")
    public CommonResponse getStoresCouponList(Authentication authentication, @PathVariable Long storeId) {
        Long customerId = Long.parseLong(authentication.getName());
        List<CustomerCouponResponse> res = couponService.getStoresCouponList(customerId, storeId);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .data(res)
                .message("쿠폰 리스트 가져오기 성공")
                .build();
    }

    // 쿠폰 발급
    @PostMapping("/{couponId}")
    public CommonResponse issueCoupon(Authentication authentication, @PathVariable Long couponId) {
        Long customerId = Long.parseLong(authentication.getName());
        couponService.issueCoupon(customerId, couponId);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("쿠폰 발급 성공")
                .build();
    }
}
