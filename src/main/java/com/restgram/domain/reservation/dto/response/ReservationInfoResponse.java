package com.restgram.domain.reservation.dto.response;

import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.entity.ReservationState;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ReservationInfoResponse(

    Long id,
    LocalDateTime datetime,
    Integer headCount, // 인원수
    String name, // 예약자명
    String phone, // 예약자 핸드폰
    String memo, // 메세지
    ReservationState state,
    String cancelMessage
    
) {

  public static ReservationInfoResponse of(Reservation reservation) {
    return ReservationInfoResponse.builder()
        .id(reservation.getId())
        .datetime(reservation.getDatetime())
        .headCount(reservation.getHeadCount())
        .name(reservation.getName())
        .phone(reservation.getPhone())
        .memo(reservation.getMemo())
        .state(reservation.getState())
        .build();
  }

  public static ReservationInfoResponse of(Reservation reservation, String cancelMessage) {
    return ReservationInfoResponse.builder()
        .id(reservation.getId())
        .datetime(reservation.getDatetime())
        .headCount(reservation.getHeadCount())
        .name(reservation.getName())
        .phone(reservation.getPhone())
        .memo(reservation.getMemo())
        .state(reservation.getState())
        .cancelMessage(cancelMessage)
        .build();
  }
}
