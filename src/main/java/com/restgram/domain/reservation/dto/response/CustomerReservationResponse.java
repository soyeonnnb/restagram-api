package com.restgram.domain.reservation.dto.response;

import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.user.dto.response.StoreInfoResponse;
import lombok.Builder;

@Builder
public record CustomerReservationResponse(

    StoreInfoResponse store,
    ReservationInfoResponse reservation

) {

  public static CustomerReservationResponse of(Reservation reservation) {
    return CustomerReservationResponse.builder()
        .reservation(ReservationInfoResponse.of(reservation))
        .store(StoreInfoResponse.of(reservation.getStore()))
        .build();
  }

  public static CustomerReservationResponse of(Reservation reservation, String message) {
    return CustomerReservationResponse.builder()
        .reservation(ReservationInfoResponse.of(reservation, message))
        .store(StoreInfoResponse.of(reservation.getStore()))
        .build();
  }
}
