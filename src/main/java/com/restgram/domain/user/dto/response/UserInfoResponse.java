package com.restgram.domain.user.dto.response;

import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.entity.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String nickname;
    private String profileImage;
    private String type;

    public static UserInfoResponse of(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .type(user.getProfileImage())
                .build();
    }
}
