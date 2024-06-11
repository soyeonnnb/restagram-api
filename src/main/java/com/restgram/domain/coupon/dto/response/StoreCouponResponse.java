package com.restgram.domain.coupon.dto.response;

import com.restgram.domain.coupon.entity.Coupon;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreCouponResponse {

    private Long id;
    private LocalDateTime startAt;
    private LocalDateTime finishAt;
    private Long quantity;
    private Long remainQuantity;
    private Long useQuantity;
    private Integer discountMoney;
    private Integer payMoney;
    private Long expiredMinute;
    private boolean disable;

    public static StoreCouponResponse of(Coupon coupon, Long useQuantity) {
        return StoreCouponResponse.builder()
                .id(coupon.getId())
                .startAt(coupon.getStartAt())
                .finishAt(coupon.getFinishAt())
                .quantity(coupon.getQuantity())
                .remainQuantity(coupon.getRemainQuantity())
                .useQuantity(useQuantity)
                .discountMoney(coupon.getDiscountMoney())
                .payMoney(coupon.getPayMoney())
                .disable(coupon.getDisable())
                .expiredMinute(coupon.getExpiredMinute())
                .build();
    }

}
