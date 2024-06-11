package com.restgram.domain.reservation.entity;

import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.Store;
import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "reservation_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ReservationForm reservationForm; // 예약폼

    @JoinColumn(name = "store_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store; // 가게

    @JoinColumn(name = "customer_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer; // 예약자

    private LocalDateTime datetime; // 예약 시간

    @Column(nullable = false)
    @ColumnDefault("2")
    @Min(0)
    private Integer headCount; // 인원수

    private String name; // 예약자명
    private String phone; // 예약자 핸드폰
    private String memo; // 메세지

    @Enumerated(EnumType.STRING)
    private ReservationState state; // 예약 상태

    public void updateState(ReservationState state) {
        this.state = state;
    }

}
