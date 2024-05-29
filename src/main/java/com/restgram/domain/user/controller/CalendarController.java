package com.restgram.domain.user.controller;

import com.restgram.domain.user.dto.request.CalendarAgreeRequest;
import com.restgram.domain.user.dto.response.CalendarAgreeResponse;
import com.restgram.domain.user.service.CalendarService;
import com.restgram.global.exception.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/calendar")
@Slf4j
public class CalendarController {

    private final CalendarService calendarService;

    @PatchMapping("/agree")
    public CommonResponse customerCalendarAgree(Authentication authentication, CalendarAgreeRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        CalendarAgreeResponse response = calendarService.customerCalendarAgree(userId, request);
        return CommonResponse.builder()
                .data(response)
                .code(HttpStatus.OK.value())
                .success(true)
                .message("유저 정보 업데이트 완료")
                .build();
    }
}
