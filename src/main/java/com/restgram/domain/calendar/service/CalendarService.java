package com.restgram.domain.calendar.service;

import com.restgram.domain.calendar.dto.request.CalendarAgreeRequest;
import com.restgram.domain.calendar.dto.response.CalendarAgreeResponse;
import com.restgram.domain.user.entity.Customer;

public interface CalendarService {
    CalendarAgreeResponse agreeCalendar(Long userId, CalendarAgreeRequest request);
    void createCalendar(Customer customer);
}
