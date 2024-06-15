package com.restgram.domain.user.controller;

import com.restgram.domain.user.dto.request.NicknameRequest;
import com.restgram.domain.user.dto.response.CheckResponse;
import com.restgram.domain.user.dto.response.FeedUserInfoResponse;
import com.restgram.domain.user.dto.response.UserInfoCursorResponse;
import com.restgram.domain.user.service.UserService;
import com.restgram.global.exception.entity.ApiResponse;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<ApiResponse<?>> logout(@CookieValue(value = TYPE_ACCESS) String accessToken,
                                                 @CookieValue(value = TYPE_REFRESH) String refreshToken, HttpServletResponse response) {
        userService.logout(response, accessToken, refreshToken);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<?>> reIssueToken(
            @Nullable @CookieValue(value = TYPE_ACCESS) String accessToken,
            @Nullable @CookieValue(value = TYPE_REFRESH) String refreshToken,
            HttpServletResponse response) {
        userService.reissue(response, accessToken, refreshToken);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.CREATED);
    }

    // 유저 리스트 검색
    @GetMapping
    public ResponseEntity<ApiResponse<UserInfoCursorResponse>> searchUser(@RequestParam(value = "cursor-id", required = false) Long cursorId,
                                                                          @RequestParam("query") String query) {

        UserInfoCursorResponse userInfoCursorResponse = userService.searchUser(cursorId, query);

        return new ResponseEntity<>(ApiResponse.createSuccess(userInfoCursorResponse), HttpStatus.OK);
    }

    // 유저 상세 정보 가져오기
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<FeedUserInfoResponse>> getUserInfo(
            Authentication authentication, @PathVariable("userId") Long userId) {
        Long myId = Long.parseLong(authentication.getName());
        FeedUserInfoResponse feedUserInfoResponse = userService.getFeedUser(myId, userId);

        return new ResponseEntity<>(ApiResponse.createSuccess(feedUserInfoResponse), HttpStatus.OK);
    }

    // 유저 닉네임 변경
    @PatchMapping("/nickname")
    public ResponseEntity<ApiResponse<?>> updateNickname(Authentication authentication,
                                                         @RequestBody @Valid NicknameRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        userService.updateNickname(userId, request);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
    }

    // 유저 프로필 이미지 변경
    @PatchMapping("/image")
    public ResponseEntity<ApiResponse<?>> updateImage(Authentication authentication,
                                                      @RequestPart(name = "image") @Nullable MultipartFile image) {
        Long userId = Long.parseLong(authentication.getName());
        userService.updateProfileImage(userId, image);

        return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);

    }

    // 유저 닉네임 중복확인
    @GetMapping("/duplicate/nickname")
    public ResponseEntity<ApiResponse<CheckResponse>> duplicateNickname(
            @RequestParam("query") String query) {
        CheckResponse checkResponse = userService.duplicateNickname(query);

        return new ResponseEntity<>(ApiResponse.createSuccess(checkResponse), HttpStatus.OK);
    }

}

