package com.restgram.domain.reservation.service;

import com.restgram.domain.reservation.dto.request.AddReservationRequest;
import com.restgram.domain.reservation.dto.request.DeleteReservationRequest;
import com.restgram.domain.reservation.dto.response.StoreReservationResponse;
import com.restgram.global.entity.PaginationResponse;

import java.util.List;

public interface ReservationService {
    void addReservation(Long userId, AddReservationRequest request);

    void cancelReservation(Long userId, DeleteReservationRequest request);

    PaginationResponse getCustomerReservations(Long userId, Long cursorId);

    List<StoreReservationResponse> getStoreReservations(Long userId, Integer year, Integer month);
}
