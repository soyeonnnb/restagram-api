package com.restgram.domain.reservation.repository;

import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.entity.ReservationCancel;
import com.restgram.domain.reservation.entity.ReservationState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationCancelRepository extends JpaRepository<ReservationCancel, Long> {
    Optional<ReservationCancel> findByReservation(Reservation reservation);
}
