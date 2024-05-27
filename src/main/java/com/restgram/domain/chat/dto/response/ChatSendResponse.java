package com.restgram.domain.chat.dto.response;

import com.restgram.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Builder
public class ChatSendResponse {
    private List<Long> userIds;
    private ChatMessageResponse message;
}
