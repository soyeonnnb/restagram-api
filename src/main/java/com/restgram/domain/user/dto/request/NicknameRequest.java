package com.restgram.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class NicknameRequest {
    @NotBlank(message = "닉네임은 필수 영역입니다.")
    @Size(min = 1, max = 30, message = "닉네임은 최소 1자 ~ 최대 30자까지 입력 가능합니다.")
    private String nickname;
}
