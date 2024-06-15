package com.restgram.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    private String email; // 이메일

    @Column(nullable = false)
    private String uid; // uid

    @Enumerated(EnumType.STRING)
    private LoginMethod loginMethod; // 로그인 방법

    @Column(nullable = false)
    private String accessToken; // 액세스 토큰

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean calendarAgree; // 캘린더 동의 현황

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean updateCalendarAgree(boolean calendarAgree) {
        this.calendarAgree = calendarAgree;
        return this.calendarAgree;
    }

    public void updateEmail(String email) {
        this.email = email;
    }
}
