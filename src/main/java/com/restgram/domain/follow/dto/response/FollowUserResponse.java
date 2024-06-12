package com.restgram.domain.follow.dto.response;

import com.restgram.domain.user.dto.response.UserInfoResponse;
import com.restgram.domain.user.entity.User;
import lombok.Builder;

@Builder
public record FollowUserResponse(

    Long id,
    UserInfoResponse user,
    boolean isFollowed
    
) {

  public static FollowUserResponse of(Long id, User user, boolean isFollowed) {
    return FollowUserResponse.builder()
        .id(id)
        .user(UserInfoResponse.of(user))
        .isFollowed(isFollowed)
        .build();
  }
}
