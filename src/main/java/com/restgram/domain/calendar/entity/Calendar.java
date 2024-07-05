package com.restgram.domain.calendar.entity;

import com.restgram.domain.user.entity.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }
        Calendar that = (Calendar) obj;
        return this.calendarId.equals(that.calendarId);
    }

    @Override
    public int hashCode() {
        return this.calendarId.hashCode();
    }
}
