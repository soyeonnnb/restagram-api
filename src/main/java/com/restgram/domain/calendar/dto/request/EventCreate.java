package com.restgram.domain.calendar.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.restgram.domain.reservation.entity.Reservation;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class EventCreate {

    @NotBlank(message = "일정 제목은 필수 영역입니다.")
    private String title;

    @NotBlank(message = "일정 시간은 필수 영역입니다.")
    private EventTime time;

    @Nullable
    private Integer[] reminders;

    @Nullable
    private String color;

    static class EventTime {
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        @NotBlank(message = "일정 시작 시간은 필수 영역입니다.")
        private LocalDateTime start_at;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        @NotBlank(message = "일정 종료 시간은 필수 영역입니다.")
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
