package com.restgram.domain.reservation.service;

import com.restgram.domain.reservation.dto.request.ReservationFormRequest;
import com.restgram.domain.reservation.dto.request.UpdateReservationFormRequest;
import com.restgram.domain.reservation.dto.response.ReservationFormResponse;

import java.util.List;

public interface ReservationFormService {
    void addReservationForm(Long userId, ReservationFormRequest req);
    List<ReservationFormResponse> getReservationForm(Long storeId, Integer year, Integer month);
    void updateReservationState(Long storeId, UpdateReservationFormRequest request);

    List<ReservationFormResponse> getStoreReservationForm(Long storeId, Integer year, Integer month);
}
