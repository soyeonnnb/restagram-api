package com.restgram.domain.user.controller;

import com.restgram.domain.user.dto.request.JoinRequest;
import com.restgram.domain.user.dto.request.LoginRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.service.StoreService;
import com.restgram.global.exception.entity.CommonResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
@Slf4j
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PostMapping("/join")
    public CommonResponse join(@Valid @RequestBody JoinRequest req) {
        storeService.join(req);
        return CommonResponse.builder()
                .data(null)
                .message("SUCCESS")
                .code(HttpStatus.CREATED.value())
                .success(true)
                .build()
                ;
    }

    @PostMapping("/login")
    public CommonResponse login(@Valid @RequestBody LoginRequest req, HttpServletResponse response) {
        LoginResponse res = storeService.login(req, response);
        return CommonResponse.builder()
                .data(res)
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .success(true)
                .build()
                ;
    }
}
