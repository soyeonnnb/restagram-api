package com.restgram.domain.chat.service.impl;

import com.restgram.domain.chat.dto.request.ChatMessageRequest;
import com.restgram.domain.chat.dto.response.ChatMessageResponse;
import com.restgram.domain.chat.dto.response.ChatSendResponse;
import com.restgram.domain.chat.entity.ChatMessage;
import com.restgram.domain.chat.entity.ChatRoom;
import com.restgram.domain.chat.repository.ChatMessageRepository;
import com.restgram.domain.chat.repository.ChatRoomRepository;
import com.restgram.domain.chat.service.ChatService;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.ChatErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional
    public ChatSendResponse sendChat(ChatMessageRequest request) {
        User sender = userRepository.findById(request.userId()).orElseThrow(
                () -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + request.userId() + "]"));
        ChatRoom chatRoom = chatRoomRepository.findById(request.roomId()).orElseThrow(
                () -> new RestApiException(ChatErrorCode.INVALID_CHATROOM_ID,
                        "채팅방 ID가 유효하지 않습니다. [채팅방ID=" + request.roomId() + "]"));

        // 채팅방 멤버들의 User 리스트 생성
        List<Long> memberIds = chatRoom.getMembers().stream().map(user -> user.getUser().getId())
                .collect(Collectors.toList());

        // 해당 유저가 참가한 채팅방이 아니면
        if (!memberIds.contains(request.userId())) {
            throw new RestApiException(ChatErrorCode.NOT_USER_CHATROOM,
                    "로그인 사용자가 참여한 채팅방이 아닙니다. [로그인 사용자ID=" + request.userId() + ", 채팅방ID="
                            + request.roomId() + "]");
        }

        // 메세지 저장
        ChatMessage message = request.toEntity(sender, chatRoom);
        chatMessageRepository.save(message);

        // 메세지 설정
        chatRoom.updateLastMessage(message);

        return ChatSendResponse.builder()
                .message(ChatMessageResponse.of(message))
                .userIds(memberIds)
                .build();
    }
}
