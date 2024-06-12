package com.restgram.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(

    @NotNull(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    String email,

    @NotNull(message = "비밀번호는 필수 입력값입니다.")
    String password
    
) {

}
