package com.restgram.domain.user.controller;

import com.restgram.domain.user.dto.request.UpdateCustomerRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.UserAddressListResponse;
import com.restgram.domain.user.service.CustomerService;
import com.restgram.global.exception.entity.ApiResponse;
import jakarta.annotation.Nullable;
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

    // 유저 주소 업데이트
    @PatchMapping("/address")
    public ResponseEntity<ApiResponse<?>> updateUserAddress(Authentication authentication, @RequestParam("address-id") @Nullable Long addressId, @RequestParam("range") Integer range) {
        Long userId = Long.parseLong(authentication.getName());
        customerService.updateUserAddress(userId, addressId, range);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
    }

    // 유저 주소변경 버튼 클릭 시 관련 주소 가져오기
    @GetMapping("/address")
    public ResponseEntity<ApiResponse<UserAddressListResponse>> getUserAddressList(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        UserAddressListResponse userAddressListResponse = customerService.getUserAddressList(userId);

        return new ResponseEntity<>(ApiResponse.createSuccess(userAddressListResponse), HttpStatus.OK);
    }

    // 유저 정보 가져오기
    @PatchMapping
    public ResponseEntity<ApiResponse<?>> updateCustomer(Authentication authentication, @RequestBody UpdateCustomerRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        customerService.updateCustomer(userId, request);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
    }

}
