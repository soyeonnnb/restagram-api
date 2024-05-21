package com.restgram.domain.reservation.controller;

import com.restgram.domain.reservation.dto.request.AddReservationRequest;
import com.restgram.domain.reservation.service.ReservationService;
import com.restgram.global.exception.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation")
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    // 구매자 예약 등록
    @PostMapping
    public CommonResponse addReservation(Authentication authentication, @RequestBody AddReservationRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        reservationService.addReservation(userId, request);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("예약이 성공적으로 등록되었습니다.")
                .build();
    }

    // 예약 취소

    
    // 예약리스트 가져오기
    
    
    // 예약 상세 가져오기

    
}
