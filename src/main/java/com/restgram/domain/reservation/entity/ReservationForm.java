package com.restgram.domain.reservation.entity;

import com.restgram.domain.user.entity.Store;
import com.restgram.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Time;
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

    @JoinColumn(name = "store_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    private LocalDate date;

    private LocalTime time;

    private Integer quantity;

    private Integer remainQuantity;

    private Integer tablePerson; // 테이블 당 인원수

    private Integer maxReservationPerson; // 최대 예약 인원수

    @Enumerated(EnumType.STRING)
    private ReservationFormState state;

    public void updateRemainQuantity(Integer tableNum) {
        this.remainQuantity += tableNum;
    }
}
