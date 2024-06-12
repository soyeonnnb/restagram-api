package com.restgram.domain.chat.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record ChatSendResponse(

    List<Long> userIds,
    ChatMessageResponse message
    
) {

}
