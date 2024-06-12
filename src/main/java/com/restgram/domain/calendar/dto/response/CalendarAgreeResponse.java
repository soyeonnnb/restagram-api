package com.restgram.domain.calendar.dto.response;

import lombok.Builder;

@Builder
public record CalendarAgreeResponse(

    boolean agree
) {

}
