package com.restgram.domain.calendar.entity;

import com.restgram.domain.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }
        CalendarEvent that = (CalendarEvent) obj;
        return this.eventId.equals(that.eventId);
    }

    @Override
    public int hashCode() {
        return this.eventId.hashCode();
    }
}
