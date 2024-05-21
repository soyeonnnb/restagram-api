package com.restgram.domain.reservation.service;

import com.restgram.domain.reservation.dto.request.AddReservationRequest;
import com.restgram.domain.reservation.dto.request.DeleteReservationRequest;
import com.restgram.domain.reservation.dto.response.CustomerReservationResponse;
import com.restgram.domain.reservation.dto.response.StoreReservationResponse;

import java.util.List;

public interface ReservationService {
    void addReservation(Long userId, AddReservationRequest request);

    void cancelReservation(Long userId, DeleteReservationRequest request);

    List<CustomerReservationResponse> getCustomerReservations(Long userId);

    List<StoreReservationResponse> getStoreReservations(Long userId, Integer year, Integer month);
}
