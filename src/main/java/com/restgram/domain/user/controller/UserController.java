package com.restgram.domain.user.controller;

import com.restgram.domain.user.dto.request.LoginRequest;
import com.restgram.domain.user.dto.request.NicknameRequest;
import com.restgram.domain.user.dto.request.UpdatePasswordRequest;
import com.restgram.domain.user.dto.response.CheckResponse;
import com.restgram.domain.user.dto.response.FeedUserInfoResponse;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.UserInfoResponse;
import com.restgram.domain.user.service.UserService;
import com.restgram.global.exception.entity.CommonResponse;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.management.monitor.CounterMonitor;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final String TYPE_ACCESS = "access";
    private final String TYPE_REFRESH = "refresh";
    private final UserService userService;

    @PostMapping("/login")
    public CommonResponse login(@Valid @RequestBody LoginRequest req, HttpServletResponse response) {
        LoginResponse res = userService.login(req, response);
        return CommonResponse.builder()
                .data(res)
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .success(true)
                .build()
                ;
    }

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

    @GetMapping
    public CommonResponse searchUser(@RequestParam("query") String query) {
        List<UserInfoResponse> response = userService.searchUser(query);
        return CommonResponse.builder()
                .data(response)
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .success(true)
                .build()
                ;
    }

    @GetMapping("/{userId}")
    public CommonResponse getUserInfo(Authentication authentication, @PathVariable Long userId) {
        Long myId = Long.parseLong(authentication.getName());
        FeedUserInfoResponse response = userService.getFeedUser(myId, userId);
        return CommonResponse.builder()
                .data(response)
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .success(true)
                .build()
                ;
    }

    @PatchMapping("/password")
    public CommonResponse updatePassword(Authentication authentication, @RequestBody @Valid UpdatePasswordRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        userService.updatePassword(userId, request);
        return CommonResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .success(true)
                .build()
                ;
    }

    @GetMapping("/nickname")
    public CommonResponse duplicateNickname(@RequestBody NicknameRequest request) {
        CheckResponse check = userService.duplicateNickname(request);
        return CommonResponse.builder()
                .data(check)
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .success(true)
                .build()
                ;
    }

}

