package com.restgram.domain.reservation.repository;

import com.restgram.domain.coupon.entity.Coupon;
import com.restgram.domain.reservation.entity.ReservationForm;
import com.restgram.domain.reservation.entity.ReservationFormState;
import com.restgram.domain.user.entity.Store;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface ReservationFormRepository extends JpaRepository<ReservationForm, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ) // 비관적 락 사용
    @EntityGraph(attributePaths = {"store"})
    Optional<ReservationForm> findById(Long id);

    boolean existsByStoreAndDate(Store store, LocalDate date);
    List<ReservationForm> findAllByStoreAndDateBetweenAndStateEquals(Store store, LocalDate startAt, LocalDate finishAt, ReservationFormState state);
    List<ReservationForm> findAllByStoreAndDateBetween(Store store, LocalDate startAt, LocalDate finishAt);
    Optional<ReservationForm> findByStoreAndDateAndTime(Store store, LocalDate date, LocalTime time);
}
