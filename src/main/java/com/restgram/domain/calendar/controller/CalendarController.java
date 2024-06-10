package com.restgram.domain.calendar.controller;

import com.restgram.domain.calendar.dto.request.CalendarAgreeRequest;
import com.restgram.domain.calendar.dto.response.CalendarAgreeResponse;
import com.restgram.domain.calendar.service.CalendarService;
import com.restgram.global.exception.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/calendar")
@Slf4j
public class CalendarController {

    private final CalendarService calendarService;

    // 캘린더 동의 현황 변경
    @PatchMapping("/agree")
    public CommonResponse customerCalendarAgree(Authentication authentication, @RequestBody CalendarAgreeRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        CalendarAgreeResponse response = calendarService.agreeCalendar(userId, request);
        return CommonResponse.builder()
                .data(response)
                .code(HttpStatus.OK.value())
                .success(true)
                .message("캘린더 정보 업데이트 완료")
                .build();
    }
}
