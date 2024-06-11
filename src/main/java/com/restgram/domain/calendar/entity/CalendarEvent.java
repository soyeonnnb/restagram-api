package com.restgram.domain.calendar.entity;

import com.restgram.domain.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CalendarEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation; // 캘린더와 관련된 예약

    @Column(nullable = false)
    private String eventId; // 일정 ID
}
