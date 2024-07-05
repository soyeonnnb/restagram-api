package com.restgram.domain.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ReservationCancel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation; // 예약

    @Enumerated(EnumType.STRING)
    private ReservationCancelState state; // 취소 상태

    private String memo; // 취소 사유

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }

        ReservationCancel that = (ReservationCancel) obj;

        return this.reservation.equals(that.reservation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.reservation);
    }
}
