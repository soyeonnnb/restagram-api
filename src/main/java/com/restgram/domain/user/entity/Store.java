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
    private String email; // 이메이

    private String storeName; // 가게명
    private Double latitude; // 위도
    private Double longitude; // 경고

    @JoinColumn(name = "emd_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EmdAddress emdAddress; // 가게 위치(읍면동)

    private String address; // 가게 주소
    private String detailAddress; // 가게 상세 주소
    private String storePhone; // 가게 번호

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