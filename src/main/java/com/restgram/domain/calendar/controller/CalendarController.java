package com.restgram.domain.calendar.controller;

import com.restgram.domain.calendar.dto.request.CalendarAgreeRequest;
import com.restgram.domain.calendar.dto.response.CalendarAgreeResponse;
import com.restgram.domain.calendar.service.CalendarService;
import com.restgram.global.exception.entity.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<CalendarAgreeResponse>> customerCalendarAgree(Authentication authentication, @RequestBody CalendarAgreeRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        CalendarAgreeResponse calendarAgreeResponse = calendarService.agreeCalendar(userId, request);

        return new ResponseEntity<>(ApiResponse.createSuccess(calendarAgreeResponse), HttpStatus.CREATED);
    }
}
