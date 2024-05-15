package com.restgram.domain.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String email;
    private String nickname;
    private String type;

}
