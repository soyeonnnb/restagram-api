package com.restgram.domain.coupon.dto.response;

import com.restgram.domain.coupon.entity.IssueCoupon;
import com.restgram.domain.user.dto.response.StoreInfoResponse;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record IssueCouponResponse(

    Long id,
    Boolean isUsed,
    LocalDateTime usedAt,
    String qrImage,
    LocalDateTime expiredAt,
    Integer discountMoney,
    Integer payMoney,
    StoreInfoResponse store
    
) {

  public static IssueCouponResponse of(IssueCoupon coupon) {
    return IssueCouponResponse.builder()
        .id(coupon.getId())
        .isUsed(coupon.getIsUsed())
        .usedAt(coupon.getUsedAt())
        .expiredAt(coupon.getExpiredAt())
        .discountMoney(coupon.getCoupon().getDiscountMoney())
        .payMoney(coupon.getCoupon().getPayMoney())
        .store(StoreInfoResponse.of(coupon.getCoupon().getStore()))
        .build();
  }
}
