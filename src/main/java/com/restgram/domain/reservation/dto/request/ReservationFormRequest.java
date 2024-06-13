package com.restgram.domain.reservation.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ReservationFormRequest(

        @FutureOrPresent(message = "예약 시작 날짜는 현재 이후만 가능합니다.")
        @NotNull(message = "예약 시작 날짜는 필수 영역입니다.")
        LocalDate startAt, //  시작 날짜

        @FutureOrPresent(message = "예약 종료 날짜는 현재 이후만 가능합니다.")
        @NotNull(message = "예약 종료 날짜는 필수 영역입니다.")
        LocalDate finishAt, // 종료 날짜

        List<LocalDate> exceptDateList, // 제외 날짜

        @NotNull(message = "등록 폼은 최소 1개 이상 있어야 합니다.")
        Map<DayOfWeek, List<ReservationFormDateRequest>> weekListMap

) {

}
