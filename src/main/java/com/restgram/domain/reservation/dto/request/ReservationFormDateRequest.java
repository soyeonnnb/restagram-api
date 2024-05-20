package com.restgram.domain.reservation.dto.request;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class ReservationFormDateRequest {
    private LocalTime time;
    private Integer table;
}
