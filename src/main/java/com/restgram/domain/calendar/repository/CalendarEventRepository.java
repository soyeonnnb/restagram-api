package com.restgram.domain.calendar.repository;

import com.restgram.domain.calendar.entity.CalendarEvent;
import com.restgram.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
    boolean existsByReservation(Reservation reservation);

    Optional<CalendarEvent> findByReservation(Reservation reservation);
}
