package com.restgram.domain.coupon.dto.request;

import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.user.entity.Store;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AddCouponRequest(

    @Future(message = "쿠폰 시작 시간은 현재 시간 이후만 가능합니다.")
    @NotNull(message = "쿠폰 시작 시간은 필수 영역입니다.")
    LocalDateTime startAt,

    @Future(message = "쿠폰 종료 시간은 현재 시간 이후만 가능합니다.")
    @NotNull(message = "쿠폰 종료 시간은 필수 영역입니다.")
    LocalDateTime finishAt,

    @Min(value = 1, message = "쿠폰 개수는 최소 1개부터 가능합니다.")
    @NotNull(message = "쿠폰 개수는 필수 영역입니다.")
    Long quantity,

    @Min(value = 1, message = "할인 금액은 최소 1원부터 가능합니다.")
    @NotNull(message = "할인 금액은 필수 영역입니다.")
    Integer discountMoney,

    @Min(value = 0, message = "최소 주문 금액은 음수가 될 수 없습니다.")
    @NotNull(message = "최소 주문 금액은 필수 영역입니다.")
    Integer payMoney,

    @Min(value = 0, message = "쿠폰 만료 시간은 음수가 될 수 없습니다.")
    @NotNull(message = "쿠폰 만료 시간은 필수 영역입니다.")
    Long expiredMinute

) {

  public Coupon toEntity(Store store) {
    return Coupon.builder()
        .store(store)
        .startAt(this.startAt)
        .finishAt(this.finishAt)
        .quantity(this.quantity)
        .remainQuantity(this.quantity)
        .disable(false)
        .discountMoney(this.discountMoney)
        .payMoney(this.payMoney)
        .expiredMinute(this.expiredMinute)
        .build();
  }
}
