package com.restgram.domain.chat.controller;

import com.restgram.domain.chat.dto.response.ChatRoomResponse;
import com.restgram.domain.chat.service.ChatRoomService;
import com.restgram.global.exception.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat/room")
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

    // 내 채팅방 리스트 가져오기
    @GetMapping
    public CommonResponse getChatRoomList(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<ChatRoomResponse> response = chatRoomService.getChatRoomList(userId);
        return CommonResponse.builder()
                .message("채팅방 가져오기")
                .data(response)
                .code(HttpStatus.OK.value())
                .success(true)
                .build();
    }

}
