package com.restgram.domain.reservation.service;

import com.restgram.domain.reservation.dto.request.ReservationFormRequest;

public interface ReservationFormService {
    void addReservationForm(Long userId, ReservationFormRequest req);
}
