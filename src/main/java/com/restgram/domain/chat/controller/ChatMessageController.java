package com.restgram.domain.chat.controller;

import com.restgram.domain.chat.dto.response.ChatMessageResponse;
import com.restgram.domain.chat.service.ChatMessageService;
import com.restgram.global.exception.entity.ApiResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    // 내 채팅방 리스트 가져오기
    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getChatList(Authentication authentication, @PathVariable("roomId") @NotNull Long roomId) {
        Long userId = Long.parseLong(authentication.getName());
        List<ChatMessageResponse> chatMessageResponseList = chatMessageService.getChatList(userId, roomId);

        return new ResponseEntity<>(ApiResponse.createSuccess(chatMessageResponseList), HttpStatus.OK);
    }

}
