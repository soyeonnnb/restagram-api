package com.restgram.domain.calendar.dto.request;

import jakarta.validation.constraints.NotNull;

public record CalendarAgreeRequest(

    @NotNull(message = "동의 여부는 필수 영역입니다.")
    boolean agree
    
) {

}
