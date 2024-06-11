package com.restgram.domain.coupon.entity;

import com.restgram.domain.user.entity.Store;
import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "store_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store; // 쿠폰 발급 가게

    @Column(nullable = false)
    private LocalDateTime startAt; // 쿠폰 발급 시작시간

    @Column(nullable = false)
    private LocalDateTime finishAt; // 쿠폰 발급 만료시간

    @Column(nullable = false)
    @ColumnDefault("1")
    private Long quantity; // 쿠폰 개수

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long remainQuantity; // 쿠폰 남은 개수

    @Column(nullable = false)
    @ColumnDefault("1")
    private Integer discountMoney; // 할인 금액

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer payMoney; // 최소 주문 금액

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long expiredMinute; // 만료 시간

    @ColumnDefault("false")
    private Boolean disable; // 쿠폰 발급 가능상태

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
    public void issueCoupon() {
        this.remainQuantity--;
    }

}
