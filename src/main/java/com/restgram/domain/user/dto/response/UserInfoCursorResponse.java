package com.restgram.domain.user.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserInfoCursorResponse(

        Long cursorId,
        List<UserInfoResponse> list,
        boolean hasNext
) {
}
