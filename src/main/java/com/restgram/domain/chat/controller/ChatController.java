package com.restgram.domain.chat.controller;

import com.restgram.domain.chat.dto.request.ChatMessageRequest;
import com.restgram.domain.chat.dto.response.ChatSendResponse;
import com.restgram.domain.chat.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // 메세지 전송
    @MessageMapping("/chat")
    public void chat(@Payload @Valid ChatMessageRequest request) {
        log.info("채팅시작");
        ChatSendResponse sendResponse = chatService.sendChat(request);
        MessageHeaders headers = new MessageHeaders(
                Collections.singletonMap(MessageHeaders.CONTENT_TYPE,
                        MimeTypeUtils.APPLICATION_JSON_VALUE));
        // 해당 방을 구독하는 사용자에게 메세지 전달
        simpMessagingTemplate.convertAndSend("/sub/room/" + request.roomId(), sendResponse.message(), headers);
//        for (Long userId : sendResponse.userIds()) {
//            simpMessagingTemplate.convertAndSend("/sub/chat/" + userId, sendResponse.message(),
//                    headers);
//        }
    }

}
