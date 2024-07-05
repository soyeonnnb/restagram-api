package com.restgram.domain.coupon.entity;

import com.restgram.domain.user.entity.Customer;
import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssueCoupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "customer_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer; // 쿠폰 발급 회원

    @JoinColumn(name = "coupon_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Coupon coupon; // 쿠폰

    @Column(nullable = false)
    private LocalDateTime expiredAt; // 만료시간

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isUsed; // 사용여부

    private LocalDateTime usedAt; // 사용시간

    public void use() {
        isUsed = true;
        usedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }

        IssueCoupon that = (IssueCoupon) obj;

        return this.customer.equals(that.customer) &&
                this.coupon.equals(that.coupon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.customer, this.coupon);
    }

}
