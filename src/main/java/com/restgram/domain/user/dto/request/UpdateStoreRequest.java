package com.restgram.domain.user.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
        Double longitude,

        @NotNull(message = "테이블 당 인원수는 필수 영역입니다.")
        @Min(value = 1, message = "테이블 당 인원수는 1명 이상이여야 합니다.")
        Integer tablePerson, // 테이블 당 인원수

        @NotNull(message = "최대 예약 인원수는 필수 영역입니다.")
        @Min(value = 0, message = "최대 예약 인원수는 음수가 될 수 없습니다.")
        @Max(value = 20, message = "최대 예약 인원수는 최대 20까지 가능합니다.")
        Integer maxReservationPerson // 최대 예약 인원수

) {

}
