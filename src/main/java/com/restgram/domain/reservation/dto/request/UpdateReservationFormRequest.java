package com.restgram.domain.reservation.dto.request;

import com.restgram.domain.reservation.entity.ReservationFormState;
import lombok.Getter;

@Getter
public class UpdateReservationFormRequest {
    private Long id;
    private ReservationFormState state;
}
