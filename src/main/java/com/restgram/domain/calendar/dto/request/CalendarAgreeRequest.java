package com.restgram.domain.calendar.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CalendarAgreeRequest {
    @NotNull(message = "동의 여부는 필수 영역입니다.")
    private boolean agree;
}
