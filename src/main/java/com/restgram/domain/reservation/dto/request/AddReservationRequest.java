package com.restgram.domain.reservation.dto.request;

import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.entity.ReservationForm;
import com.restgram.domain.reservation.entity.ReservationState;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.Store;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
public class AddReservationRequest {
    @NotNull(message = "인원 수는 필수 영역입니다.")
    @Min(value = 1, message = "예약 인원은 최소 1명 이여야 합니다.")
    @Max(value = 2000, message = "예약 인원은 최대 1000명까지 가능합니다.")
    private Integer headCount; // 인원수

    @NotNull(message = "예약폼 ID는 필수 영역입니다.")
    @Min(value = 0, message = "예약폼 ID는 음수가 될 수 없습니다.")
    private Long reservationFormId;

    @NotBlank(message = "예약자 명은 필수 영역입니다.")
    private String name; // 예약자명

    @NotBlank(message = "예약자 핸드폰은 필수 영역입니다.")
    private String phone; // 예약자 핸드폰

    private String memo; // 메세지

    public Reservation of(ReservationForm form, Store store, Customer customer) {
        return Reservation.builder()
                .reservationForm(form)
                .store(store)
                .customer(customer)
                .datetime(LocalDateTime.of(form.getDate(), form.getTime()))
                .headCount(headCount)
                .name(name)
                .phone(phone)
                .memo(memo)
                .state(ReservationState.ACTIVE)
                .build();
    }
}
