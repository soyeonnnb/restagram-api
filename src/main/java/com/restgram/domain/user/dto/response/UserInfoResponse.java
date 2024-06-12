package com.restgram.domain.user.dto.response;

import com.restgram.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserInfoResponse(

    Long id,
    String nickname,
    String profileImage,
    String type
) {

  public static UserInfoResponse of(User user) {
    return UserInfoResponse.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .profileImage(user.getProfileImage())
        .type(user.getType())
        .build();
  }
}
