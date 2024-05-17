package com.restgram.domain.coupon.entity;

import com.restgram.domain.user.entity.Customer;
import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssueCoupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "customer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @JoinColumn(name = "coupon_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Coupon coupon;
    private LocalDateTime expiredAt;
    private Boolean isUsed;
    private LocalDateTime usedAt;
    private String qrImage;

    public void setQrImage(String qrImage) {
        this.qrImage = qrImage;
    }
}
