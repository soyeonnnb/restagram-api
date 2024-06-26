package com.restgram.domain.reservation.repository;

import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.entity.ReservationState;
import com.restgram.domain.user.entity.Store;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {
    @Lock(LockModeType.PESSIMISTIC_READ) // 비관적 락 사용
    @EntityGraph(attributePaths = {"customer"})
    Optional<Reservation> findById(Long id);

    @EntityGraph(attributePaths = {"customer"})
    List<Reservation> findAllByStoreAndDatetimeGreaterThanEqualAndDatetimeLessThanOrderByDatetime(Store store, LocalDateTime startAt, LocalDateTime endAt);

    @EntityGraph(attributePaths = {"customer"})
    List<Reservation> findAllByDatetimeAndState(LocalDateTime dateTime, ReservationState state);

}
