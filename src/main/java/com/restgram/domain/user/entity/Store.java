package com.restgram.domain.user.entity;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.user.dto.request.UpdateStoreRequest;
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

    @Column(nullable = false)
    private String email;

    private String storeName;
    private Double latitude;
    private Double longitude;

    @JoinColumn(name = "emd_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EmdAddress emdAddress;

    private String address;
    private String detailAddress;
    private String storePhone;

    public void updateStoreInfo(UpdateStoreRequest request, EmdAddress emdAddress) {
        this.storeName = request.getStoreName();
        this.storePhone = request.getStorePhone();
        this.emdAddress = emdAddress;
        this.address = request.getAddress();
        this.detailAddress = request.getDetailAddress();
        this.latitude = request.getLatitude();
        this.longitude = request.getLongitude();
    }
}