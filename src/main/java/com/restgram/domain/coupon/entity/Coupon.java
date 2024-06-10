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
    private Store store;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime finishAt;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Min(0)
    private Long quantity;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Min(0)
    private Long remainQuantity;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Min(0)
    private Integer discountMoney;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Min(0)
    private Integer payMoney;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long expiredMinute;

    @ColumnDefault("false")
    private Boolean disable;

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
    public void issueCoupon() {
        this.remainQuantity--;
    }

}
