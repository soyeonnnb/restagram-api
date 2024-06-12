package com.restgram.domain.reservation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationFormDateRequest(

    @NotNull(message = "예약 폼 시간은 필수 영역입니다.")
    LocalTime time,

    @NotNull(message = "예약 테이블 수는 필수 영역입니다.")
    @Min(value = 0, message = "테이블 수는 음수가 될 수 없습니다.")
    Integer table
) {

}
