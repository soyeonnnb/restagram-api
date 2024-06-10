package com.restgram.domain.user.controller;

import com.restgram.domain.user.dto.request.NicknameRequest;
import com.restgram.domain.user.dto.request.UpdatePasswordRequest;
import com.restgram.domain.user.dto.response.*;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final String TYPE_ACCESS = "access";
    private final String TYPE_REFRESH = "refresh";
    private final UserService userService;

    // 회원 로그아웃
    @PostMapping("/logout")
    public CommonResponse logout(@CookieValue(value = TYPE_ACCESS) String accessToken, @CookieValue(value = TYPE_REFRESH) String refreshToken, HttpServletResponse response) {
        log.info("로그아웃");
        userService.logout(response, accessToken, refreshToken);
        return CommonResponse.builder()
                .data(null)
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .success(true)
                .build()
                ;
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public CommonResponse reIssueToken(@Nullable @CookieValue(value = TYPE_ACCESS) String accessToken, @Nullable @CookieValue(value = TYPE_REFRESH) String refreshToken, HttpServletResponse response) {
        log.info("토큰 재발급");
        userService.reissue(response, accessToken, refreshToken);
        return CommonResponse.builder()
                .data(null)
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .success(true)
                .build()
                ;
    }

    // 유저 리스트 검색
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

    // 유저 상세 정보 가져오기
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

    // 유저 패스워드 변경
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

    // 유저 닉네임 변경
    @PatchMapping("/nickname")
    public CommonResponse updateNickname(Authentication authentication, @RequestBody NicknameRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        userService.updateNickname(userId, request);
        return CommonResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .success(true)
                .build()
                ;
    }

    // 유저 프로필 이미지 변경
    @PatchMapping("/image")
    public CommonResponse updateImage(Authentication authentication, @RequestPart(name = "image") @Nullable MultipartFile image) {
        Long userId = Long.parseLong(authentication.getName());
        UserProfileResponse response = userService.updateProfileImage(userId, image);
        return CommonResponse.builder()
                .message("SUCCESS")
                .data(response)
                .code(HttpStatus.OK.value())
                .success(true)
                .build()
                ;

    }

    // 유저 닉네임 중복확인
    @GetMapping("/duplicate/nickname")
    public CommonResponse duplicateNickname(@RequestParam("query") String query) {
        CheckResponse check = userService.duplicateNickname(query);
        return CommonResponse.builder()
                .data(check)
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .success(true)
                .build()
                ;
    }

}

