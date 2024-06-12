package com.restgram.domain.user.dto.request;

import lombok.Builder;

@Builder
public record CreateCalenderRequest(

    String name,
    String color,
    Integer reminder_all_day

) {

}
