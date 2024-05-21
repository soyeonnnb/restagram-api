package com.restgram.domain.coupon.dto.response;

import com.restgram.domain.coupon.entity.IssueCoupon;
import com.restgram.domain.user.dto.response.StoreInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueCouponResponse {
    private Long id;
    private Boolean isUsed;
    private LocalDateTime usedAt;
    private String qrImage;
    private LocalDateTime expiredAt;
    private Integer discountMoney;
    private Integer payMoney;
    private StoreInfoResponse store;

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
