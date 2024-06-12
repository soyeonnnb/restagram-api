package com.restgram.domain.user.controller;

import com.restgram.domain.user.dto.request.LoginRequest;
import com.restgram.domain.user.dto.request.StoreJoinRequest;
import com.restgram.domain.user.dto.request.UpdatePasswordRequest;
import com.restgram.domain.user.dto.request.UpdateStoreRequest;
import com.restgram.domain.user.dto.response.CheckResponse;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.StoreInfoResponse;
import com.restgram.domain.user.service.StoreService;
import com.restgram.global.exception.entity.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/store")
@Slf4j
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;

  // 가게 회원 회원가입
  @PostMapping("/join")
  public ResponseEntity<ApiResponse<?>> join(@Valid @RequestBody StoreJoinRequest req) {
    storeService.join(req);

    return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.CREATED);
  }

  // 가게 회원 로그인
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest req,
      HttpServletResponse response) {
    LoginResponse loginResponse = storeService.login(req, response);

    return new ResponseEntity<>(ApiResponse.createSuccess(loginResponse), HttpStatus.OK);
  }

  // 이름과 닉네임으로 가게 조회
  @GetMapping
  public ResponseEntity<ApiResponse<List<StoreInfoResponse>>> searchByNameAndNickname(
      @RequestParam String name) {
    List<StoreInfoResponse> storeInfoResponseList = storeService.searchByName(name);

    return new ResponseEntity<>(ApiResponse.createSuccess(storeInfoResponseList), HttpStatus.OK);
  }

  // 가게 정보 변경
  @PatchMapping
  public ResponseEntity<ApiResponse<?>> updateStore(Authentication authentication,
      @RequestBody UpdateStoreRequest request) {
    Long userId = Long.parseLong(authentication.getName());
    storeService.updateStore(userId, request);

    return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
  }


  // 유저 패스워드 변경
  @PatchMapping("/password")
  public ResponseEntity<ApiResponse<?>> updatePassword(Authentication authentication,
      @RequestBody @Valid UpdatePasswordRequest request) {
    Long userId = Long.parseLong(authentication.getName());
    storeService.updatePassword(userId, request);

    return new ResponseEntity<>(ApiResponse.createSuccess(null), HttpStatus.OK);
  }


  @GetMapping("/duplicate/email")
  public ResponseEntity<ApiResponse<CheckResponse>> duplicateEmail(
      @RequestParam("query") String query) {
    CheckResponse checkResponse = storeService.duplicateEmail(query);

    return new ResponseEntity<>(ApiResponse.createSuccess(checkResponse), HttpStatus.OK);
  }
}
