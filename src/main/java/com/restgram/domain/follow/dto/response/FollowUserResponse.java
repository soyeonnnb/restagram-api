package com.restgram.domain.follow.dto.response;

import com.restgram.domain.follow.entity.Follow;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowUserResponse {
    private Long id;
    private Long userId;
    private String nickname;
    private String profileImage;
    private String type;
    private boolean isFollowed;
    public static FollowUserResponse of(Long id, User user, boolean isFollowed) {
        return FollowUserResponse.builder()
                .id(id)
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .type(user.getType())
                .isFollowed(isFollowed)
                .build();
    }
}
