package com.restgram.domain.user.entity;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.user.dto.request.UpdateStoreRequest;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("STORE")
public class Store extends User {

  @Column(nullable = false)
  private String email; // 이메일
  private String password; // 비밀번호


  private String storeName; // 가게명
  private Double latitude; // 위도
  private Double longitude; // 경고

  @JoinColumn(name = "emd_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private EmdAddress emdAddress; // 가게 위치(읍면동)

  private String address; // 가게 주소
  private String detailAddress; // 가게 상세 주소
  private String storePhone; // 가게 번호

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