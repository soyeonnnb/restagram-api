package com.restgram.domain.user.controller;

import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.service.CustomerService;
import com.restgram.global.exception.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
