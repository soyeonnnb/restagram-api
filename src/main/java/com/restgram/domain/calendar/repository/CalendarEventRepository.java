package com.restgram.domain.calendar.repository;

import com.restgram.domain.calendar.entity.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
}
