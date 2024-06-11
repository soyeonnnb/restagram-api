package com.restgram.domain.chat.entity;

import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "last_message_id")
    private ChatMessage lastMessage; // 마지막 전송 메세지

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMember> members = new ArrayList<>(); // 채팅 멤버

    public void updateLastMessage(ChatMessage message) {
        this.lastMessage = message;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id=" + id +
                ", lastMessage=" + lastMessage +
                ", members=" + members +
                '}';
    }
}
