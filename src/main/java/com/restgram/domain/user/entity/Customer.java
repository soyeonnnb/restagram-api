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
public class Customer extends User{

    private String email;

    @Column(nullable = false)
    private String uid;

    @Enumerated(EnumType.STRING)
    private LoginMethod loginMethod;

    @JoinColumn(name = "emd_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EmdAddress emdAddress;

    @JoinColumn(name = "sigg_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SiggAddress siggAddress;

    @JoinColumn(name = "sido_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SidoAddress sidoAddress;

    @ColumnDefault("0")
    private Integer addressRange;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean calendarAgree;

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
