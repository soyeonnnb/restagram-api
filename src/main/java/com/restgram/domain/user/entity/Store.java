package com.restgram.domain.user.entity;

import com.restgram.domain.address.entity.EmdAddress;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("STORE")
public class Store extends User {
    private String storeName;

    @Column(nullable = false)
    private String user_id;

    private String password;
    private Double latitude;
    private Double longitude;

    @JoinColumn(name = "emd_id")
    @ManyToOne
    private EmdAddress address;

    private String detailAddress;
    private String storePhone;
}
