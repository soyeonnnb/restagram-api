package com.restgram.domain.reservation.controller;

import com.restgram.domain.reservation.dto.request.ReservationFormRequest;
import com.restgram.domain.reservation.dto.request.UpdateReservationFormStateRequest;
import com.restgram.domain.reservation.dto.response.ReservationFormResponse;
import com.restgram.domain.reservation.service.ReservationFormService;
import com.restgram.global.exception.entity.ApiResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservation/form")
@Slf4j
public class ReservationFormController {

    private final ReservationFormService reservationFormService;

    // 가게 예약 등록
    @PostMapping
    public ResponseEntity<ApiResponse<?>> addReservation(Authentication authentication, @RequestBody @Valid ReservationFormRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        reservationFormService.addReservationForm(userId, request);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.CREATED);
    }

    // 구매자 -> 가게 예약 폼 가져오기
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponse<List<ReservationFormResponse>>> getReservationForm(@PathVariable("storeId") @NonNull Long storeId, @RequestParam("year") @NonNull Integer year, @RequestParam("month") @NonNull Integer month) {
        List<ReservationFormResponse> reservationFormResponseList = reservationFormService.getReservationForm(storeId, year, month);

        return new ResponseEntity<>(ApiResponse.createSuccess(reservationFormResponseList), HttpStatus.OK);
    }

    // 가게 -> 자기가게
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationFormResponse>>> getStoreReservationForm(Authentication authentication, @RequestParam("year") @NonNull Integer year, @RequestParam("month") @NonNull Integer month) {
        Long storeId = Long.parseLong(authentication.getName());
        List<ReservationFormResponse> reservationFormResponseList = reservationFormService.getStoreReservationForm(storeId, year, month);

        return new ResponseEntity<>(ApiResponse.createSuccess(reservationFormResponseList), HttpStatus.OK);
    }

    // 예약 상태 변경
    @PatchMapping
    public ResponseEntity<ApiResponse<?>> getUserReservationFormState(Authentication authentication, @RequestBody @Valid UpdateReservationFormStateRequest request) {
        Long storeId = Long.parseLong(authentication.getName());
        reservationFormService.updateReservationState(storeId, request);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
    }
}
