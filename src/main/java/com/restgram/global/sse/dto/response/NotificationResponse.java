package com.restgram.global.sse.dto.response;

import com.restgram.global.sse.entity.Notification;
import com.restgram.global.sse.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private Long userId;
    private String content;
    private NotificationType type;
    private Long relatedId;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationResponse of(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUser().getId())
                .content(notification.getContent())
                .type(notification.getType())
                .relatedId(notification.getRelatedId())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();

    }
}
