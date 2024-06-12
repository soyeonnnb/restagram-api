package com.restgram.domain.reservation.dto.response;

import com.restgram.domain.reservation.entity.ReservationForm;
import com.restgram.domain.reservation.entity.ReservationFormState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;

@Builder
public record ReservationFormResponse(

    Long id,

    LocalDate date,

    LocalTime time,

    Integer quantity,

    Integer remainQuantity,

    Integer tablePerson, // 테이블 당 인원수

    Integer maxReservationPerson, // 최대 예약 인원수

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
        .tablePerson(form.getTablePerson())
        .maxReservationPerson(form.getMaxReservationPerson())
        .state(form.getState())
        .build();
  }
}
