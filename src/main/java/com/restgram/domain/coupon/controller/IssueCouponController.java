package com.restgram.domain.coupon.controller;

import com.restgram.domain.coupon.dto.response.IssueCouponResponse;
import com.restgram.domain.coupon.service.IssueCouponService;
import com.restgram.global.exception.entity.ApiResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/coupon/issue")
@Slf4j
public class IssueCouponController {

    private final IssueCouponService issueCouponService;

    //구매자 쿠폰 전체 가져오기
    @GetMapping
    public ResponseEntity<ApiResponse<List<IssueCouponResponse>>> getCustomerCouponList(Authentication authentication) {
        Long customerId = Long.parseLong(authentication.getName());
        List<IssueCouponResponse> issueCouponResponseList = issueCouponService.getCustomerCouponList(customerId);

        return new ResponseEntity<>(ApiResponse.createSuccess(issueCouponResponseList), HttpStatus.OK);
    }

    // 쿠폰 사용
    @PostMapping("/{couponId}")
    public ResponseEntity<ApiResponse<?>> useCoupon(Authentication authentication, @PathVariable("couponId") @NotNull Long couponId) {
        Long customerId = Long.parseLong(authentication.getName());
        issueCouponService.useCoupon(customerId, couponId);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
    }
}
