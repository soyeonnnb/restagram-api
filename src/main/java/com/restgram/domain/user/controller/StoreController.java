package com.restgram.domain.user.controller;

import com.restgram.domain.follow.dto.response.FollowUserResponse;
import com.restgram.domain.user.dto.request.StoreJoinRequest;
import com.restgram.domain.user.dto.request.LoginRequest;
import com.restgram.domain.user.dto.request.UpdateStoreRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.StoreInfoResponse;
import com.restgram.domain.user.service.StoreService;
import com.restgram.global.exception.entity.CommonResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
@Slf4j
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PostMapping("/join")
    public CommonResponse join(@Valid @RequestBody StoreJoinRequest req) {
        storeService.join(req);
        return CommonResponse.builder()
                .data(null)
                .message("SUCCESS")
                .code(HttpStatus.CREATED.value())
                .success(true)
                .build()
                ;
    }

    @GetMapping
    public CommonResponse searchByNameAndNickname(@RequestParam String name) {
        List<StoreInfoResponse> storeInfoResponseList = storeService.searchByName(name);
        return CommonResponse.builder()
                .success(true)
                .data(storeInfoResponseList)
                .code(HttpStatus.OK.value())
                .message("가게 리스트 가져오기")
                .build();
    }

    @PatchMapping
    public CommonResponse updateStore(Authentication authentication, @RequestBody UpdateStoreRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        storeService.updateStore(userId, request);
        return CommonResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.CREATED.value())
                .success(true)
                .build()
                ;
    }
}
