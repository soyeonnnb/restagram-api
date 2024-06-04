package com.restgram.domain.reservation.dto.request;

import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.entity.ReservationCancel;
import com.restgram.domain.reservation.entity.ReservationCancelState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class DeleteReservationRequest {
    @NonNull
    private Long reservationId;
    @NotBlank
    private String memo;
    @NonNull
    private ReservationCancelState state;

    public ReservationCancel of(Reservation reservation) {
        return ReservationCancel.builder()
                .reservation(reservation)
                .state(state)
                .memo(memo)
                .build();
    }
}
