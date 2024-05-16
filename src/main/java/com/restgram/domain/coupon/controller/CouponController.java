package com.restgram.domain.coupon.controller;

import com.restgram.domain.coupon.dto.request.AddCouponReq;
import com.restgram.domain.coupon.dto.response.StoreCouponRes;
import com.restgram.domain.coupon.service.CouponService;
import com.restgram.global.exception.entity.CommonResponse;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.awt.color.CMMException;
import java.util.List;

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

    @GetMapping
    public CommonResponse getAvailableCouponList(Authentication authentication) {
        Long id = Long.parseLong(authentication.getName());
        List<StoreCouponRes> res = couponService.getAvailableCouponList(id);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .data(res)
                .message("쿠폰 리스트 가져오기 성공")
                .build();
    }

    @GetMapping("/finish")
    public CommonResponse getFinishCouponList(Authentication authentication) {
        Long id = Long.parseLong(authentication.getName());
        List<StoreCouponRes> res = couponService.getFinsihCouponList(id);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .data(res)
                .message("쿠폰 리스트 가져오기 성공")
                .build();
    }
}
