package com.restgram.domain.coupon.controller;

import com.restgram.domain.coupon.dto.request.AddCouponReq;
import com.restgram.domain.coupon.service.CouponService;
import com.restgram.global.exception.entity.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/coupon")
@Slf4j
public class CouponController {

    private final CouponService couponService;

    // 쿠폰 발급
    @PostMapping
    public CommonResponse registerCoupon(Authentication authentication, @RequestBody @Valid AddCouponReq req) {
        Long id = Long.parseLong(authentication.getName());
        couponService.addCoupon(id, req);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("쿠폰 발급 완료")
                .build();
    }
    
    // 쿠폰 발급 종료
    @PostMapping("/stop/{couponId}")
    public CommonResponse stopIssueCoupon(Authentication authentication, @PathVariable Long couponId) {
        Long id = Long.parseLong(authentication.getName());
        couponService.stopCoupon(id, couponId);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("쿠폰 발급 취소 완료")
                .build();
    }

}
