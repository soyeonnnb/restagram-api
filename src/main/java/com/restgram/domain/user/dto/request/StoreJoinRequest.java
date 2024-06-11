package com.restgram.domain.user.dto.request;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.user.entity.Store;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class StoreJoinRequest {

  @Email(message = "이메일 형식이 아닙니다.")
  @NotBlank(message = "이메일은 필수 영역입니다.")
  @Size(min = 1, max = 350, message = "이메일은 최소 1자 ~ 최대 350자까지 입력 가능합니다.")
  private String email;

  @NotBlank(message = "비밀번호는 필수 영역입니다.")
  @Size(min = 1, max = 20, message = "비밀번호는 최소 1자 ~ 최대 20자까지 입력 가능합니다.")
  private String password;

  @NotBlank(message = "닉네임은 필수 영역입니다.")
  @Size(min = 1, max = 30, message = "닉네임은 최소 1자 ~ 최대 30자까지 입력 가능합니다.")
  private String nickname;

  @NotBlank(message = "이름은 필수 영역입니다.")
  @Size(min = 1, max = 30, message = "이름은 최소 1자 ~ 최대 30자까지 입력 가능합니다.")
  private String name;

  @NotBlank(message = "전화번호는 필수 영역입니다.")
  private String phone;

  @NotBlank(message = "가게 주소는 필수 영역입니다.")
  private String storeName;

  private String storePhone;

  @NotNull(message = "행정 코드는 필수 영역입니다.")
  private Long bcode;

  @NotBlank(message = "주소는 필수 영역입니다.")
  private String address;

  @NotBlank(message = "상세주소는 필수 영역입니다.")
  private String detailAddress;

  @NotNull(message = "위도는 필수 영역입니다.")
  private Double latitude;

  @NotNull(message = "경도는 필수 영역입니다.")
  private Double longitude;

  public Store of(EmdAddress emdAddress, String encodedPassword) {
    return Store.builder()
        .email(email)
        .name(name)
        .phone(phone)
        .nickname(nickname)
        .storeName(storeName)
        .password(encodedPassword)
        .latitude(latitude)
        .longitude(longitude)
        .emdAddress(emdAddress)
        .address(address)
        .detailAddress(detailAddress)
        .storePhone(storePhone)
        .build();
  }

}
