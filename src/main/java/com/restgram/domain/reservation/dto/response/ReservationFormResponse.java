package com.restgram.domain.reservation.dto.response;

import com.restgram.domain.reservation.entity.ReservationForm;
import com.restgram.domain.reservation.entity.ReservationFormState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record ReservationFormResponse(

        Long id,

        LocalDate date,

        LocalTime time,

        Integer quantity,

        Integer remainQuantity,

        @Enumerated(EnumType.STRING)
        ReservationFormState state

) {

    public static ReservationFormResponse of(ReservationForm form) {
        return ReservationFormResponse.builder()
                .id(form.getId())
                .date(form.getDate())
                .time(form.getTime())
                .quantity(form.getQuantity())
                .remainQuantity(form.getRemainQuantity())
                .state(form.getState())
                .build();
    }
}
