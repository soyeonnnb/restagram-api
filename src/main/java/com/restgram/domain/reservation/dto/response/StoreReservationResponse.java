package com.restgram.domain.reservation.dto.response;

import com.restgram.domain.reservation.entity.Reservation;
import lombok.Builder;

@Builder
public record StoreReservationResponse(

    Long customerId,
    String customerNickname,
    ReservationInfoResponse reservation
    
) {

  public static StoreReservationResponse of(Reservation reservation) {
    return StoreReservationResponse.builder()
        .customerId(reservation.getCustomer().getId())
        .customerNickname(reservation.getCustomer().getNickname())
        .reservation(ReservationInfoResponse.of(reservation))
        .build();
  }

  public static StoreReservationResponse of(Reservation reservation, String message) {
    return StoreReservationResponse.builder()
        .customerId(reservation.getCustomer().getId())
        .customerNickname(reservation.getCustomer().getNickname())
        .reservation(ReservationInfoResponse.of(reservation, message))
        .build();
  }
}
