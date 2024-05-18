package com.restgram.domain.user.controller;

import com.restgram.domain.user.dto.request.CustomerJoinRequest;
import com.restgram.domain.user.dto.request.StoreJoinRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.UserAddressListResponse;
import com.restgram.domain.user.service.CustomerService;
import com.restgram.global.exception.entity.CommonResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/info")
    public CommonResponse getInfo(Authentication authentication) {
        Long id = Long.parseLong(authentication.getName());
        LoginResponse res = customerService.getUserInfo(id);
        return CommonResponse.builder()
                .data(res)
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .success(true)
                .build()
                ;
    }

    @PostMapping("/join")
    public CommonResponse customerJoin(@Valid @RequestBody CustomerJoinRequest req) {
        customerService.join(req);
        return CommonResponse.builder()
                .data(null)
                .message("SUCCESS")
                .code(HttpStatus.CREATED.value())
                .success(true)
                .build()
                ;
    }

    // 유저 주소 업데이트
    @PatchMapping("/address")
    public CommonResponse updateUserAddress(Authentication authentication, @RequestParam("address-id") @Nullable Long addressId, @RequestParam("range") Integer range) {
        Long userId = Long.parseLong(authentication.getName());
        customerService.updateUserAddress(userId, addressId, range);
        return CommonResponse.builder()
                .code(HttpStatus.OK.value())
                .success(true)
                .message("업데이트 완료")
                .build();
    }

    // 유저 주소변경 버튼 클릭 시 관련 주소 가져오기
    @GetMapping("/address")
    public CommonResponse getUserAddressList(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        UserAddressListResponse response = customerService.getUserAddressList(userId);
        return CommonResponse.builder()
                .code(HttpStatus.OK.value())
                .data(response)
                .success(true)
                .message("가져오기 완료")
                .build();
    }
}
