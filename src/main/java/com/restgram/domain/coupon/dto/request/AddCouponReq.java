package com.restgram.domain.coupon.dto.request;

import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.user.entity.Store;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCouponReq {

    @Future
    private LocalDateTime startAt;

    @Future
    private LocalDateTime finishAt;

    @Min(0)
    private Long quantity;

    @Min(0)
    private Integer discountMoney;

    @Min(0)
    private Integer payMoney;

    public Coupon of(Store store) {
        return Coupon.builder()
                .store(store)
                .startAt(this.startAt)
                .finishAt(this.finishAt)
                .quantity(this.quantity)
                .remainQuantity(this.quantity)
                .discountMoney(this.discountMoney)
                .payMoney(this.payMoney)
                .build();
    }
}
