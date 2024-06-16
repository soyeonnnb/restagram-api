package com.restgram.domain.user.controller;

import com.restgram.domain.user.dto.request.UpdateCustomerRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.StoreInfoResponse;
import com.restgram.domain.user.service.CustomerService;
import com.restgram.global.entity.PaginationResponse;
import com.restgram.global.exception.entity.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    // 유저 정보 가져오기
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<LoginResponse>> getInfo(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        LoginResponse loginResponse = customerService.getUserInfo(userId);

        return new ResponseEntity<>(ApiResponse.createSuccess(loginResponse), HttpStatus.OK);
    }

    // 가게 예약 시 필요한 스토어 유저 정보
    @GetMapping("/store")
    public ResponseEntity<ApiResponse<PaginationResponse>> getStoreList(@RequestParam(value = "cursor-id", required = false) Long cursorId,
                                                                        @RequestParam("query") String query) {
        PaginationResponse storeInfoResponse = customerService.getStoreList(cursorId, query);

        return new ResponseEntity<>(ApiResponse.createSuccess(storeInfoResponse), HttpStatus.OK);
    }

    // 가게 예약 시 필요한 스토어 유저 정보
    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse<StoreInfoResponse>> getStoreInfo(@PathVariable("storeId") @NotNull Long storeId) {
        StoreInfoResponse storeInfoResponse = customerService.getStoreInfo(storeId);

        return new ResponseEntity<>(ApiResponse.createSuccess(storeInfoResponse), HttpStatus.OK);
    }

    // 유저 정보 수정하기
    @PatchMapping
    public ResponseEntity<ApiResponse<?>> updateCustomer(Authentication authentication, @RequestBody @Valid UpdateCustomerRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        customerService.updateCustomer(userId, request);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
    }

}
