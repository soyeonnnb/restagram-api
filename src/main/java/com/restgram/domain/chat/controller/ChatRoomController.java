package com.restgram.domain.chat.controller;

import com.restgram.domain.chat.dto.response.ChatRoomResponse;
import com.restgram.domain.chat.service.ChatRoomService;
import com.restgram.global.exception.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/room")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 가져오기 -> 없으면 생성
    @PostMapping
    public CommonResponse getChatRoom(Authentication authentication, @RequestParam("receiver-id") Long receiverId) {
        Long userId = Long.parseLong(authentication.getName());
        ChatRoomResponse response = chatRoomService.getChatRoom(userId, receiverId);
        return CommonResponse.builder()
                .message("특정 채팅방 가져오기")
                .data(response)
                .code(HttpStatus.OK.value())
                .success(true)
                .build();
    }

}
