package com.restgram.domain.coupon.dto.response;

import com.restgram.domain.coupon.entity.Coupon;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CustomerCouponResponse(

    Long id,
    LocalDateTime startAt,
    LocalDateTime finishAt,
    Long quantity,
    Long remainQuantity,
    Integer discountMoney,
    Integer payMoney,
    Long expiredMinute,
    boolean isIssued // 발급 여부

) {

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
