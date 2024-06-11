package com.restgram.domain.calendar.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CalendarEventAddRequest {
    @NotBlank(message = "캘린더 ID는 필수 영역입니다.")
    @Min(value = 0, message = "캘린더 ID는 음수가 될 수 없습니다.")
    private String calendar_id;

    @NotBlank(message = "일정 Event는 필수 영역입니다.")
    private EventCreate event;
}
