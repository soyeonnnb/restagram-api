package com.restgram.domain.reservation.repository;

import com.restgram.domain.reservation.entity.ReservationForm;
import com.restgram.domain.user.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ReservationFormRepository extends JpaRepository<ReservationForm, Long> {
    boolean existsByStoreAndDate(Store store, LocalDate date);
}
