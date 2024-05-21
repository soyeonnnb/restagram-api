package com.restgram.domain.coupon.dto.request;

import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.user.entity.Store;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCouponRequest {

    @Future
    @NotNull
    private LocalDateTime startAt;

    @Future
    @NotNull
    private LocalDateTime finishAt;

    @Min(0)
    @NotNull
    private Long quantity;

    @Min(0)
    @NotNull
    private Integer discountMoney;

    @Min(0)
    @NotNull
    private Integer payMoney;

    @Min(0)
    @NotNull
    private Long expiredMinute;

    public Coupon of(Store store) {
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
