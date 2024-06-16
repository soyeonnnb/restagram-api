package com.restgram.domain.reservation.repository;

import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.user.entity.Customer;

import java.util.List;

public interface ReservationRepositoryCustom {
    List<Reservation> findAllByCustomerAndDatetimeBeforeThanOrderByDatetimeDesc(Customer customer, Long cursorId);
}
