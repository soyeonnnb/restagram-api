package com.restgram.domain.reservation.dto.request;

import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.entity.ReservationCancel;
import com.restgram.domain.reservation.entity.ReservationCancelState;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class DeleteReservationRequest {
    @NotNull(message = "예약 ID는 필수 영역입니다.")
    @Min(value = 0, message = "예약 ID는 음수가 될 수 없습니다.")
    private Long reservationId;

    private String memo;

    @NotBlank(message = "취소 주체는 필수 영역입니다.")
    private ReservationCancelState state;

    public ReservationCancel of(Reservation reservation) {
        return ReservationCancel.builder()
                .reservation(reservation)
                .state(state)
                .memo(memo)
                .build();
    }
}
