package com.restgram.domain.user.dto.response;

import lombok.Builder;

@Builder
public record CheckResponse(

    boolean check
    
) {

}
