package com.restgram.domain.coupon.controller;

import com.restgram.domain.coupon.dto.response.CustomerCouponRes;
import com.restgram.domain.coupon.dto.response.IssueCouponRes;
import com.restgram.domain.coupon.service.IssueCouponService;
import com.restgram.global.exception.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/coupon/issue")
@Slf4j
public class IssueCouponController {

    private final IssueCouponService issueCouponService;

    //구매자 쿠폰 전체 가져오기
    @GetMapping
    public CommonResponse getCustomerCouponList(Authentication authentication) {
        Long customerId = Long.parseLong(authentication.getName());
        List<IssueCouponRes> res = issueCouponService.getCustomerCouponList(customerId);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .data(res)
                .message("쿠폰 리스트 가져오기 성공")
                .build();
    }
}
