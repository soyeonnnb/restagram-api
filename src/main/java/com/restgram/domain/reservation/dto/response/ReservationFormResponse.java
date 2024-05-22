package com.restgram.domain.reservation.dto.response;

import com.restgram.domain.reservation.entity.ReservationForm;
import com.restgram.domain.reservation.entity.ReservationFormState;
import com.restgram.domain.user.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationFormResponse {
    private Long id;

    private LocalDate date;

    private LocalTime time;

    private Integer quantity;

    private Integer remainQuantity;

    private Integer tablePerson; // 테이블 당 인원수

    private Integer maxReservationPerson; // 최대 예약 인원수

    @Enumerated(EnumType.STRING)
    private ReservationFormState state;

    public static ReservationFormResponse of(ReservationForm form) {
        return ReservationFormResponse.builder()
                .id(form.getId())
                .date(form.getDate())
                .time(form.getTime())
                .quantity(form.getQuantity())
                .remainQuantity(form.getRemainQuantity())
                .tablePerson(form.getTablePerson())
                .maxReservationPerson(form.getMaxReservationPerson())
                .state(form.getState())
                .build();
    }
}
