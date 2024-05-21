package com.restgram.domain.reservation.dto.response;

import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.entity.ReservationForm;
import com.restgram.domain.reservation.entity.ReservationState;
import com.restgram.domain.user.entity.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationInfoResponse {
    private Long id;
    private LocalDateTime datetime;
    private Integer headCount; // 인원수
    private String name; // 예약자명
    private String phone; // 예약자 핸드폰
    private String memo; // 메세지
    private ReservationState state;
    private String cancelMessage;

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
