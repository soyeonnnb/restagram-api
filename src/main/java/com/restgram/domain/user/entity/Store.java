package com.restgram.domain.user.entity;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.user.dto.request.UpdateStoreRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
@DiscriminatorValue("STORE")
public class Store extends User {

    @Column(nullable = false)
    private String email; // 이메일

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private String storeName; // 가게명

    @Column(nullable = false)
    private Double latitude; // 위도

    @Column(nullable = false)
    private Double longitude; // 경도

    @JoinColumn(name = "emd_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EmdAddress emdAddress; // 가게 위치(읍면동) -> 검색시 필요

    @Column(nullable = false)
    private String address; // 가게 주소

    @Column(nullable = false)
    private String detailAddress; // 가게 상세 주소

    @Column(nullable = false)
    private String storePhone; // 가게 번호

    @Column(nullable = false)
    @ColumnDefault("4")
    @Min(1)
    private Integer tablePerson; // 테이블 당 인원수

    @Column(nullable = false)
    @ColumnDefault("8")
    private Integer maxReservationPerson; // 최대 예약 인원수

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateStoreInfo(UpdateStoreRequest request, EmdAddress emdAddress) {
        this.storeName = request.storeName();
        this.storePhone = request.storePhone();
        this.emdAddress = emdAddress;
        this.address = request.address();
        this.detailAddress = request.detailAddress();
        this.latitude = request.latitude();
        this.longitude = request.longitude();
    }
}