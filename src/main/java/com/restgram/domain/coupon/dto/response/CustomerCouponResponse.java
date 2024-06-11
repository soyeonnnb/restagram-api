package com.restgram.domain.coupon.dto.response;

import com.restgram.domain.coupon.entity.Coupon;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerCouponResponse {
    private Long id;
    private LocalDateTime startAt;
    private LocalDateTime finishAt;
    private Long quantity;
    private Long remainQuantity;
    private Integer discountMoney;
    private Integer payMoney;
    private Long expiredMinute;
    private boolean isIssued; // 발급 여부

    public static CustomerCouponResponse of(Coupon coupon, Boolean isIssued) {
        return CustomerCouponResponse.builder()
                .id(coupon.getId())
                .startAt(coupon.getStartAt())
                .finishAt(coupon.getFinishAt())
                .quantity(coupon.getQuantity())
                .remainQuantity(coupon.getRemainQuantity())
                .discountMoney(coupon.getDiscountMoney())
                .payMoney(coupon.getPayMoney())
                .isIssued(isIssued)
                .expiredMinute(coupon.getExpiredMinute())
                .build();
    }
}
