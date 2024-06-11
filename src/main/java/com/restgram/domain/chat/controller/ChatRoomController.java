package com.restgram.domain.chat.controller;

import com.restgram.domain.address.dto.response.AddressResponse;
import com.restgram.domain.chat.dto.response.ChatRoomResponse;
import com.restgram.domain.chat.service.ChatRoomService;
import com.restgram.global.exception.entity.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<ChatRoomResponse>> getChatRoom(Authentication authentication, @RequestParam("receiver-id") Long receiverId) {
        Long userId = Long.parseLong(authentication.getName());
        ChatRoomResponse chatRoomResponse = chatRoomService.getChatRoom(userId, receiverId);

        return new ResponseEntity<>(ApiResponse.createSuccess(chatRoomResponse), HttpStatus.OK);
    }

    // 내 채팅방 리스트 가져오기
    @GetMapping
    public ResponseEntity<ApiResponse<List<ChatRoomResponse>>> getChatRoomList(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<ChatRoomResponse> chatRoomResponseList = chatRoomService.getChatRoomList(userId);

        return new ResponseEntity<>(ApiResponse.createSuccess(chatRoomResponseList), HttpStatus.OK);
    }

}
