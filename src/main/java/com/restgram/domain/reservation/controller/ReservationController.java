package com.restgram.domain.reservation.controller;

import com.restgram.domain.reservation.dto.request.AddReservationRequest;
import com.restgram.domain.reservation.dto.request.DeleteReservationRequest;
import com.restgram.domain.reservation.dto.response.CustomerReservationResponse;
import com.restgram.domain.reservation.dto.response.StoreReservationResponse;
import com.restgram.domain.reservation.service.ReservationService;
import com.restgram.global.exception.entity.CommonResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservation")
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
    @PatchMapping
    public CommonResponse deleteReservation(Authentication authentication, @RequestBody @Valid DeleteReservationRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        reservationService.cancelReservation(userId, request);
        return CommonResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("예약이 성공적으로 취소되었습니다.")
                .build();
    }
    
    // 구매자 예약리스트 가져오기
    @GetMapping("/customer")
    public CommonResponse getCustomerReservationList(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<CustomerReservationResponse> reservationResponseList = reservationService.getCustomerReservations(userId);
        return CommonResponse.builder()
                .success(true)
                .data(reservationResponseList)
                .code(HttpStatus.OK.value())
                .message("구매자 예약 리스트")
                .build();
    }

    // 가게 예약리스트 가져오기
    @GetMapping("/store")
    public CommonResponse getStoreReservationList(Authentication authentication, @RequestParam Integer year, @RequestParam Integer month) {
        Long userId = Long.parseLong(authentication.getName());
        List<StoreReservationResponse> reservationResponseList = reservationService.getStoreReservations(userId, year, month);
        return CommonResponse.builder()
                .success(true)
                .data(reservationResponseList)
                .code(HttpStatus.OK.value())
                .message("가게 예약 리스트")
                .build();
    }
    

}
