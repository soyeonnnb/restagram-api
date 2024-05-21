package com.restgram.domain.reservation.repository;

import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.entity.ReservationForm;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.Store;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ) // 비관적 락 사용
    Optional<Reservation> findById(Long id);
    @EntityGraph(attributePaths = {"store"})
    List<Reservation> findAllByCustomerOrderByDatetimeDesc(Customer customer);

    @EntityGraph(attributePaths = {"customer"})
    List<Reservation> findAllByStoreAndDatetimeGreaterThanEqualAndDatetimeLessThan(Store store, LocalDateTime startAt, LocalDateTime endAt);
}
