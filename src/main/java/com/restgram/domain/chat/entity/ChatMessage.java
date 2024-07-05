package com.restgram.domain.chat.entity;

import com.restgram.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatMessageType type; // 채팅 메세지 타입 TALK, IMAGE

    @Column(nullable = false, length = 1000)
    private String message; // 채팅 메세지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom; // 채팅방

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author; // 작성자

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime time; // 작성 시간


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }
        ChatMessage that = (ChatMessage) obj;
        return this.message.equals(that.message) &&
                this.chatRoom.equals(that.chatRoom) &&
                this.author.equals(that.author) &&
                this.time.equals(that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.message, this.author, this.time);
    }
}
