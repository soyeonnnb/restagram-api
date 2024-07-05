package com.restgram.domain.chat.entity;

import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
    @Builder.Default
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


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }
        ChatRoom that = (ChatRoom) obj;

        // sort해서 순서도 같도록 만듦
        this.members.sort(Comparator.comparing(o -> o.getUser().getNickname()));
        that.members.sort(Comparator.comparing(o -> o.getUser().getNickname()));

        return this.members.get(0).getUser().equals(that.members.get(0).getUser()) &&
                this.members.get(1).getUser().equals(that.members.get(1).getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getMembers().get(0).getUser(), this.getMembers().get(1));
    }
}
