package com.restgram.domain.reservation.service;

import com.restgram.domain.reservation.dto.request.ReservationFormRequest;
import com.restgram.domain.reservation.dto.request.UpdateReservationFormStateRequest;
import com.restgram.domain.reservation.dto.response.ReservationFormResponse;

import java.util.List;

public interface ReservationFormService {
    void addReservationForm(Long userId, ReservationFormRequest req);
    List<ReservationFormResponse> getReservationForm(Long storeId, Integer year, Integer month);
    void updateReservationState(Long storeId, UpdateReservationFormStateRequest request);

    List<ReservationFormResponse> getStoreReservationForm(Long storeId, Integer year, Integer month);
}
