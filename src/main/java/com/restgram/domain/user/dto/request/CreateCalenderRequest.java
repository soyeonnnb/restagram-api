package com.restgram.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateCalenderRequest {
    private String name;
    private String color;
    private Integer reminder_all_day;
}
