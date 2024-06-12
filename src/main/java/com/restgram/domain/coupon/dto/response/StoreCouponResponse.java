package com.restgram.domain.coupon.dto.response;

import com.restgram.domain.coupon.entity.Coupon;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record StoreCouponResponse(

    Long id,
    LocalDateTime startAt,
    LocalDateTime finishAt,
    Long quantity,
    Long remainQuantity,
    Long useQuantity,
    Integer discountMoney,
    Integer payMoney,
    Long expiredMinute,
    boolean disable

) {

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
