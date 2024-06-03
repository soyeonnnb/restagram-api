package com.restgram.domain.calendar.entity;

import com.restgram.domain.user.entity.Customer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_customer_id", columnList = "customer_id"))
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "customer_id", nullable = false)
    @OneToOne
    private Customer customer;

    @Column(name = "calendar_id", nullable = false)
    private String calendarId;
}
