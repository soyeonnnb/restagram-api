package com.restgram.domain.user.controller;

import com.restgram.domain.user.dto.request.CustomerJoinRequest;
import com.restgram.domain.user.dto.request.StoreJoinRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.service.CustomerService;
import com.restgram.global.exception.entity.CommonResponse;
import jakarta.validation.Valid;
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
        log.info("들어옴");
        customerService.join(req);
        return CommonResponse.builder()
                .data(null)
                .message("SUCCESS")
                .code(HttpStatus.CREATED.value())
                .success(true)
                .build()
                ;
    }

}
