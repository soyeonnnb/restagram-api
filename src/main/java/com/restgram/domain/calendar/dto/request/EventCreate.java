package com.restgram.domain.calendar.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.restgram.domain.reservation.entity.Reservation;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class EventCreate {
    private String title;
    private EventTime time;
    private Integer[] reminders;
    private String color;

    static class EventTime {
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime start_at;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime end_at;
        protected EventTime(LocalDateTime start_at, LocalDateTime end_at) {
            this.start_at = start_at;
            this.end_at = end_at;
        }

    }

    public static EventCreate of(Reservation reservation) {
        return EventCreate.builder()
                .title("[예약] "+reservation.getStore().getStoreName()+"("+reservation.getHeadCount()+"인)")
                .time(new EventTime(reservation.getDatetime(), reservation.getDatetime().plusHours(1)))
                .reminders(new Integer[]{120})
                .color("LAVENDER")
                .build();

    }
}
