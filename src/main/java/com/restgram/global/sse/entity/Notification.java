package com.restgram.global.sse.entity;

import com.restgram.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_notification_user", columnList = "user_id"))
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String content;

    private Long relatedId;
    private String imageUrl;

    private boolean isRead;
    private LocalDateTime createdAt;
}
