package com.restgram.domain.calendar.entity;

import com.restgram.domain.user.entity.Customer;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "customer_id", nullable = false)
    @OneToOne
    private Customer customer; // 캘린더 소유 사용자

    @Column(name = "calendar_id", nullable = false)
    private String calendarId; // 캘린더 ID

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime agreedAt; // 동의 시간
}
