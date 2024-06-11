package com.restgram.domain.reservation.entity;

import com.restgram.domain.user.entity.Store;
import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationForm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "store_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store; // 가게

    @Column(nullable = false)
    private LocalDate date; // 날짜

    @Column(nullable = false)
    private LocalTime time; // 시간

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer quantity; // 수량

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer remainQuantity; // 잔여 수량

    @Column(nullable = false)
    @ColumnDefault("4")
    @Min(1)
    private Integer tablePerson; // 테이블 당 인원수

    @Column(nullable = false)
    @ColumnDefault("8")
    private Integer maxReservationPerson; // 최대 예약 인원수

    @Enumerated(EnumType.STRING)
    private ReservationFormState state; // 예약폼 상태

    public void updateRemainQuantity(Integer tableNum) {
        this.remainQuantity += tableNum;
    }

    public void updateState(ReservationFormState state) {
        this.state = state;
    }
}
