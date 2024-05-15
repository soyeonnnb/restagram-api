package com.restgram.domain.user.controller;

import com.restgram.domain.user.service.UserService;
import com.restgram.global.exception.entity.CommonResponse;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final String TYPE_ACCESS = "access";
    private final String TYPE_REFRESH = "refresh";
    private final UserService userService;

    @PostMapping("/logout")
    public CommonResponse logout(@CookieValue(value = TYPE_ACCESS) String accessToken, @CookieValue(value = TYPE_REFRESH) String refreshToken, HttpServletResponse response) {
        userService.logout(response, accessToken, refreshToken);
        return CommonResponse.builder()
                .data(null)
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .success(true)
                .build()
                ;
    }

    @PostMapping("/reissue")
    public CommonResponse reIssueToken(@Nullable @CookieValue(value = TYPE_ACCESS) String accessToken, @Nullable @CookieValue(value = TYPE_REFRESH) String refreshToken, HttpServletResponse response) {
        userService.reissue(response, accessToken, refreshToken);
        return CommonResponse.builder()
                .data(null)
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .success(true)
                .build()
                ;
    }
}

