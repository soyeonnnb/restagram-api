package com.restgram.domain.chat.controller;

import com.restgram.domain.chat.dto.request.ChatMessageRequest;
import com.restgram.domain.chat.dto.response.ChatSendResponse;
import com.restgram.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void chat(@Payload ChatMessageRequest request) {
        log.info("채팅 들어옴");
        ChatSendResponse sendResponse = chatMessageService.sendChat(request);
        MessageHeaders headers = new MessageHeaders(Collections.singletonMap(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE));
        for(Long userId : sendResponse.getUserIds())
            simpMessagingTemplate.convertAndSend("/sub/chat/" + userId, sendResponse.getMessage(), headers);
    }

}
