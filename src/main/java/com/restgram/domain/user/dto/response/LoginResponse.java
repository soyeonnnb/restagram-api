package com.restgram.domain.user.dto.response;

import com.restgram.domain.user.entity.User;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String email;
    private String nickname;
    private String type;
    private String profileImage;

    public static LoginResponse of(User user) {
        return LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .type(user.getType())
                .profileImage(user.getProfileImage())
                .build();
    }
}
