package com.restgram.domain.user.entity;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.address.entity.SidoAddress;
import com.restgram.domain.address.entity.SiggAddress;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("CUSTOMER")
public class Customer extends User{

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

    private Integer addressRange;

    public void updateAddress(EmdAddress emdAddress, SiggAddress siggAddress, SidoAddress sidoAddress, Integer range) {
        this.emdAddress = emdAddress;
        this.siggAddress = siggAddress;
        this.sidoAddress = sidoAddress;
        this.addressRange = range;
    }
}
