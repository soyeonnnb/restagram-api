package com.restgram.domain.user.service;

import com.restgram.domain.user.dto.request.CalendarAgreeRequest;
import com.restgram.domain.user.dto.response.CalendarAgreeResponse;
import com.restgram.domain.user.entity.Customer;

public interface CalendarService {
    CalendarAgreeResponse customerCalendarAgree(Long userId, CalendarAgreeRequest request);
    void createCalendar(Customer customer);
}
