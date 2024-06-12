package com.restgram.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateStoreRequest(

    String description,

    @NotBlank(message = "전화번호는 필수 영역입니다.")
    String phone,

    @NotBlank(message = "가게 주소는 필수 영역입니다.")
    String storeName,

    String storePhone,

    @NotNull(message = "행정 코드는 필수 영역입니다.")
    Long bcode,

    @NotBlank(message = "주소는 필수 영역입니다.")
    String address,

    @NotBlank(message = "상세주소는 필수 영역입니다.")
    String detailAddress,

    @NotBlank(message = "위도는 필수 영역입니다.")
    Double latitude,

    @NotBlank(message = "경도는 필수 영역입니다.")
    Double longitude
    
) {

}
