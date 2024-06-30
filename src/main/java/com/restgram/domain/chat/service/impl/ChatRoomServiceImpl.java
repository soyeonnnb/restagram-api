package com.restgram.domain.chat.service.impl;

import com.restgram.domain.chat.dto.response.ChatRoomResponse;
import com.restgram.domain.chat.entity.ChatMember;
import com.restgram.domain.chat.entity.ChatRoom;
import com.restgram.domain.chat.repository.ChatMemberRepository;
import com.restgram.domain.chat.repository.ChatRoomRepository;
import com.restgram.domain.chat.service.ChatRoomService;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.ChatErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;

    // 채팅방 가져오기 (없으면 생성후 가져오기)
    @Override
    @Transactional
    public ChatRoomResponse getChatRoom(Long userId, Long receiverId) {
        if (userId == receiverId)
            throw new RestApiException(ChatErrorCode.SENDER_RECEIVER_IS_THE_SAME, "채팅 수신자와 발신자가 동일합니다. [사용자ID=" + userId + "]");
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID, "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID, "채팅 수신자 ID가 유효하지 않습니다. [채팅 수신자ID=" + receiverId + "]"));

        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByUsers(user, receiver);
        ChatRoom chatRoom;
        // 만약 이전에 생성되지 않은 채팅방이라면
        if (chatRoomOptional.isEmpty()) {
            // 멤버 생성
            ChatMember member1 = ChatMember.builder().user(user).build();
            ChatMember member2 = ChatMember.builder().user(receiver).build();

            // 채팅방 생성 후
            chatRoom = ChatRoom.builder()
//                    .members(Arrays.asList(member1, member2))
                    .build();

            chatRoomRepository.save(chatRoom);

            // 멤버에 채팅방 넣기
            member1.setChatRoom(chatRoom);
            member2.setChatRoom(chatRoom);

            // 멤버 저장
            chatMemberRepository.save(member1);
            chatMemberRepository.save(member2);
        } else {
            chatRoom = chatRoomOptional.get();
        }

        return ChatRoomResponse.of(chatRoom, receiver);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getChatRoomList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID, "로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByUser(user);
        System.out.println(chatRoomList);
        List<ChatRoomResponse> chatRoomResponseList = chatRoomList.stream()
                .map(chatRoom -> {
                    User receiver = chatRoom.getMembers().stream()
                            .map(ChatMember::getUser)  // ChatMember에서 User를 추출
                            .filter(member -> !member.equals(user))  // me와 다른 user를 필터링
                            .findFirst()
                            .orElse(null);
                    return ChatRoomResponse.of(chatRoom, receiver);
                })
                .collect(Collectors.toList());
        return chatRoomResponseList;
    }
}
