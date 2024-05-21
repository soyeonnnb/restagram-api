package com.restgram.domain.reservation.service;

import com.restgram.domain.reservation.dto.request.AddReservationRequest;

public interface ReservationService {
    void addReservation(Long userId, AddReservationRequest request);
}
