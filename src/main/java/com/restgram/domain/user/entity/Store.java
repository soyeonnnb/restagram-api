package com.restgram.domain.user.entity;

import com.restgram.domain.address.entity.EmdAddress;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("STORE")
public class Store extends User {
    private String storeName;
    private Double latitude;
    private Double longitude;

    @JoinColumn(name = "emd_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EmdAddress address;

    private String detailAddress;
    private String storePhone;
}
