package com.restgram.domain.coupon.entity;

import com.restgram.domain.user.entity.Store;
import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.cglib.core.Local;

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

    @JoinColumn(name = "store_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    private LocalDateTime startAt;
    private LocalDateTime finishAt;

    private Long quantity;
    private Long remainQuantity;
    private Integer discountMoney;
    private Integer payMoney;

    @ColumnDefault("false")
    private Boolean disable;

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

}
