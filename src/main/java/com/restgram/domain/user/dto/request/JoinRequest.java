package com.restgram.domain.user.dto.request;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.user.entity.Store;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수값입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수값입니다.")
    private String nickname;

    @NotBlank(message = "이름은 필수값입니다.")
    private String name;


    @NotBlank(message = "전화번호는 필수값입니다.")
    private String phone;

    private String profileImage;

    @NotBlank(message = "가게 주소는 필수값입니다.")
    private String storeName;

    private String storePhone;

    @NotBlank(message = "주소는 필수값입니다.")
    private Long bcode;

    @NotBlank(message = "상세주소는 필수값입니다.")
    private String detailAddress;

    private Double latitude;

    private Double longitude;

    public Store of(EmdAddress emdAddress) {
        return Store.builder()
                .email(email)
                .name(name)
                .profileImage(profileImage)
                .phone(phone)
                .nickname(nickname)
                .storeName(storeName)
                .password(password)
                .latitude(latitude)
                .longitude(longitude)
                .address(emdAddress)
                .detailAddress(detailAddress)
                .storePhone(storePhone)
                .build();
    }

}
