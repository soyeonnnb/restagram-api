package com.restgram.domain.reservation.controller;

import com.restgram.domain.reservation.dto.request.AddReservationRequest;
import com.restgram.domain.reservation.dto.request.DeleteReservationRequest;
import com.restgram.domain.reservation.service.ReservationService;
import com.restgram.global.exception.entity.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @PatchMapping("/{reservation_id}")
    public CommonResponse deleteReservation(Authentication authentication, @RequestBody @Valid DeleteReservationRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        reservationService.cancelReservation(userId, request);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("예약이 성공적으로 삭제되었습니다.")
                .build();
    }
    
    // 예약리스트 가져오기
    
    
    // 예약 상세 가져오기

    
}
