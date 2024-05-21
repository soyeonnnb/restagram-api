package com.restgram.domain.reservation.dto.response;

import com.restgram.domain.reservation.entity.Reservation;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreReservationResponse {
    private Long customerId;
    private String customerNickname;
    private ReservationInfoResponse reservation;

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
