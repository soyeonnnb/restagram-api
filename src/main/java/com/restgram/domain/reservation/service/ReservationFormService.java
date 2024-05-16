package com.restgram.domain.reservation.service;

import com.restgram.domain.reservation.dto.request.ReservationFormReq;

public interface ReservationFormService {
    void addReservationForm(Long id, ReservationFormReq req);
}
