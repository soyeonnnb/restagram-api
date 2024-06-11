package com.restgram.domain.reservation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

}
