package com.restgram.domain.reservation.service;

import com.restgram.domain.reservation.dto.request.AddReservationRequest;
import com.restgram.domain.reservation.dto.request.DeleteReservationRequest;

public interface ReservationService {
    void addReservation(Long userId, AddReservationRequest request);

    void cancelReservation(Long userId, DeleteReservationRequest request);
}
