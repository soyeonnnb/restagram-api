package com.restgram.domain.reservation.controller;

import com.restgram.domain.reservation.dto.request.AddReservationRequest;
import com.restgram.domain.reservation.dto.request.DeleteReservationRequest;
import com.restgram.domain.reservation.dto.response.CustomerReservationResponse;
import com.restgram.domain.reservation.dto.response.StoreReservationResponse;
import com.restgram.domain.reservation.service.ReservationService;
import com.restgram.global.entity.PaginationResponse;
import com.restgram.global.exception.entity.ApiResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<ApiResponse<?>> addReservation(Authentication authentication, @RequestBody @Valid AddReservationRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        reservationService.addReservation(userId, request);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.CREATED);
    }

    // 예약 취소
    @PatchMapping
    public ResponseEntity<ApiResponse<?>> deleteReservation(Authentication authentication, @RequestBody @Valid DeleteReservationRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        reservationService.cancelReservation(userId, request);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
    }

    // 구매자 예약리스트 가져오기
    @GetMapping("/customer")
    public ResponseEntity<ApiResponse<PaginationResponse<List<CustomerReservationResponse>>>> getCustomerReservationList(Authentication authentication, @RequestParam("cursor-id") @Nullable Long cursorId) {
        Long userId = Long.parseLong(authentication.getName());
        PaginationResponse<List<CustomerReservationResponse>> reservationResponseList = reservationService.getCustomerReservations(userId, cursorId);

        return new ResponseEntity<>(ApiResponse.createSuccess(reservationResponseList), HttpStatus.OK);
    }

    // 가게 예약리스트 가져오기
    @GetMapping("/store")
    public ResponseEntity<ApiResponse<List<StoreReservationResponse>>> getStoreReservationList(Authentication authentication, @RequestParam("year") Integer year, @RequestParam("month") Integer month) {
        Long userId = Long.parseLong(authentication.getName());
        List<StoreReservationResponse> reservationResponseList = reservationService.getStoreReservations(userId, year, month);

        return new ResponseEntity<>(ApiResponse.createSuccess(reservationResponseList), HttpStatus.OK);
    }

}
