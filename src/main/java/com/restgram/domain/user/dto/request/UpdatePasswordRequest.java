package com.restgram.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(

        @NotBlank(message = "이전 비밀번호는 필수 영역입니다.")
        @Size(min = 1, max = 20, message = "이전 비밀번호는 최소 1자 ~ 최대 20자까지 입력 가능합니다.")
        String oldPassword,

        @NotBlank(message = "새로운 비밀번호는 필수 영역입니다.")
        @Size(min = 1, max = 20, message = "새로운 비밀번호는 최소 1자 ~ 최대 20자까지 입력 가능합니다.")
        String newPassword

) {

}
