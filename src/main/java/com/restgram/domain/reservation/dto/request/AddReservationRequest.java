package com.restgram.domain.reservation.dto.request;

import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.entity.ReservationForm;
import com.restgram.domain.reservation.entity.ReservationState;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.Store;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AddReservationRequest {
    private Integer headCount; // 인원수
    private Long reservationFormId;
    private String name; // 예약자명
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
