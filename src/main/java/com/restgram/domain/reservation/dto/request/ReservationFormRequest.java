package com.restgram.domain.reservation.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
public class ReservationFormRequest {
    @Future(message = "예약 시작 날짜는 현재 이후만 가능합니다.")
    @NotNull(message = "예약 시작 날짜는 필수 영역입니다.")
    private LocalDate startAt; //  시작 날짜

    @Future(message = "예약 종료 날짜는 현재 이후만 가능합니다.")
    @NotNull(message = "예약 종료 날짜는 필수 영역입니다.")
    private LocalDate finishAt; // 종료 날짜

    @Future(message = "예약 제외 날짜는 현재 이후만 가능합니다.")
    private List<LocalDate> exceptDateList; // 제외 날짜

    @NotNull(message = "테이블 당 인원수는 필수 영역입니다.")
    @Min(value = 0, message = "테이블 당 인원수는 음수가 될 수 없습니다.")
    private Integer tablePerson; // 테이블 당 인원수

    @NotNull(message = "최대 예약 인원수는 필수 영역입니다.")
    @Min(value = 0, message = "최대 예약 인원수는 음수가 될 수 없습니다.")
    @Max(value = 2000, message = "최대 예약 인원수는 최대 2000까지 가능합니다.")
    private Integer maxReservationPerson; // 최대 예약 인원수

    @NotNull(message = "등록 폼은 최소 1개 이상 있어야 합니다.")
    private Map<DayOfWeek, List<ReservationFormDateRequest>> weekListMap;
}
