package com.restgram.domain.chat.entity;

import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "last_message_id")
    private ChatMessage lastMessage;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMember> members = new ArrayList<>();


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
