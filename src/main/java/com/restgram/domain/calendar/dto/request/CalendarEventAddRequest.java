package com.restgram.domain.calendar.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CalendarEventAddRequest {
    private String calendar_id;
    private EventCreate event;
}
