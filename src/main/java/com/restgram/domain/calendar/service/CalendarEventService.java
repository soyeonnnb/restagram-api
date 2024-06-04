package com.restgram.domain.calendar.service;

import com.restgram.domain.reservation.entity.Reservation;

public interface CalendarEventService {

    void createCalendarEvent(Reservation reservation);
    void deleteCalendarEvent(Reservation reservation);

}
