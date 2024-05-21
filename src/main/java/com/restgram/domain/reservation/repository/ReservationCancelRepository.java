package com.restgram.domain.reservation.repository;

import com.restgram.domain.reservation.entity.ReservationCancel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationCancelRepository extends JpaRepository<ReservationCancel, Long> {
}
