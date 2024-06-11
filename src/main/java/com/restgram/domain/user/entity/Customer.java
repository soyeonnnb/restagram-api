package com.restgram.domain.user.entity;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.address.entity.SidoAddress;
import com.restgram.domain.address.entity.SiggAddress;
import jakarta.persistence.*;
import lombok.*;
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

    @JoinColumn(name = "emd_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EmdAddress emdAddress; // 현재 유저 주소(읍면동)

    @JoinColumn(name = "sigg_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SiggAddress siggAddress; // 현재 유저 주소(시군구)

    @JoinColumn(name = "sido_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SidoAddress sidoAddress; // 현재 유저 주소(시도)

    @ColumnDefault("0")
    private Integer addressRange; // 주소 검색 범위

    @Column(nullable = false)
    private String accessToken; // 액세스 토큰

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean calendarAgree; // 캘린더 동의 현황

    public void updateAddress(EmdAddress emdAddress, SiggAddress siggAddress, SidoAddress sidoAddress, Integer range) {
        this.emdAddress = emdAddress;
        this.siggAddress = siggAddress;
        this.sidoAddress = sidoAddress;
        this.addressRange = range;
    }

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
