package com.restgram.domain.coupon.controller;

import com.restgram.domain.address.dto.response.AddressResponse;
import com.restgram.domain.coupon.dto.response.IssueCouponResponse;
import com.restgram.domain.coupon.service.IssueCouponService;
import com.restgram.global.exception.entity.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
