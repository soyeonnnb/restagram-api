package com.restgram.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CalendarAgreeResponse {
    boolean agree;
}
