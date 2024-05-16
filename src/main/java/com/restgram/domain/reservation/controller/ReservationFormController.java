package com.restgram.domain.reservation.controller;

import com.restgram.domain.reservation.dto.request.ReservationFormReq;
import com.restgram.domain.reservation.service.ReservationFormService;
import com.restgram.global.exception.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation/form")
@Slf4j
public class ReservationFormController {

    private final ReservationFormService reservationFormService;

    // 가게 예약 등록
    @PostMapping
    public CommonResponse storePostReservation(Authentication authentication, @RequestBody ReservationFormReq req) {
        Long id = Long.parseLong(authentication.getName());
        reservationFormService.addReservationForm(id, req);
        return CommonResponse.builder().build();
    }

}
