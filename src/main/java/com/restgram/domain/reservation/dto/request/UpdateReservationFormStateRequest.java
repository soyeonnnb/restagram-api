package com.restgram.domain.reservation.dto.request;

import com.restgram.domain.reservation.entity.ReservationFormState;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateReservationFormStateRequest(

    @NotNull(message = "예약폼 ID는 필수 영역입니다.")
    @Min(value = 0, message = "예약폼 ID는 음수가 될 수 없습니다.")
    Long id,

    @NotNull(message = "변경 상태는 필수 영역입니다.")
    ReservationFormState state
    
) {

}
