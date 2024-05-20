package com.restgram.domain.reservation.dto.request;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
public class ReservationFormRequest {
    private LocalDate startAt; //  시작 날짜
    private LocalDate finishAt; // 종료 날짜
    private List<LocalDate> exceptDateList; // 제외 날짜
    private Integer tablePerson; // 테이블 당 인원수
    private Integer maxReservationPerson; // 최대 예약 인원수
    private Map<DayOfWeek, List<ReservationFormDateRequest>> weekListMap;
}
