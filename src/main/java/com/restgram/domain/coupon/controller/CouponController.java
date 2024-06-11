package com.restgram.domain.coupon.controller;

import com.restgram.domain.address.dto.response.AddressResponse;
import com.restgram.domain.coupon.dto.request.AddCouponRequest;
import com.restgram.domain.coupon.dto.response.CustomerCouponResponse;
import com.restgram.domain.coupon.dto.response.StoreCouponResponse;
import com.restgram.domain.coupon.service.CouponService;
import com.restgram.global.exception.entity.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/coupon")
@Slf4j
public class CouponController {

    private final CouponService couponService;

    // 쿠폰 발급
    @PostMapping
    public ResponseEntity<ApiResponse<?>> registerCoupon(Authentication authentication, @RequestBody @Valid AddCouponRequest req) {
        Long storeId = Long.parseLong(authentication.getName());
        couponService.addCoupon(storeId, req);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.CREATED);
    }
    
    // 쿠폰 발급 종료
    @PostMapping("/stop/{couponId}")
    public ResponseEntity<ApiResponse<?>> stopIssueCoupon(Authentication authentication, @PathVariable Long couponId) {
        Long storeId = Long.parseLong(authentication.getName());
        couponService.stopCoupon(storeId, couponId);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
    }

    // 발급 가능한 쿠폰 리스트 가져오기
    @GetMapping
    public ResponseEntity<ApiResponse<List<StoreCouponResponse>>> getAvailableCouponList(Authentication authentication) {
        Long storeId = Long.parseLong(authentication.getName());
        List<StoreCouponResponse> storeCouponResponseList = couponService.getAvailableCouponList(storeId);

        return new ResponseEntity<>(ApiResponse.createSuccess(storeCouponResponseList), HttpStatus.OK);
    }

    // 발급 종료 쿠폰 리스트 가져오기
    @GetMapping("/finish")
    public ResponseEntity<ApiResponse<List<StoreCouponResponse>>> getFinishCouponList(Authentication authentication) {
        Long storeId = Long.parseLong(authentication.getName());
        List<StoreCouponResponse> storeCouponResponseList = couponService.getFinsihCouponList(storeId);

        return new ResponseEntity<>(ApiResponse.createSuccess(storeCouponResponseList), HttpStatus.OK);
    }

    // 구매자 -> 가게 발급가능 쿠폰 가져오기
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponse<List<CustomerCouponResponse>>> getStoresCouponList(Authentication authentication, @PathVariable Long storeId) {
        Long customerId = Long.parseLong(authentication.getName());
        List<CustomerCouponResponse> customerCouponResponseList = couponService.getStoresCouponList(customerId, storeId);

        return new ResponseEntity<>(ApiResponse.createSuccess(customerCouponResponseList), HttpStatus.OK);
    }

    // 쿠폰 발급
    @PostMapping("/{couponId}")
    public ResponseEntity<ApiResponse<?>> issueCoupon(Authentication authentication, @PathVariable Long couponId) {
        Long customerId = Long.parseLong(authentication.getName());
        couponService.issueCoupon(customerId, couponId);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.CREATED);
    }
}
