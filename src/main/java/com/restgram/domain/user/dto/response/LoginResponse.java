package com.restgram.domain.user.dto.response;

import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.Store;
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

    public static LoginResponse of(Store store) {
        return LoginResponse.builder()
                .id(store.getId())
                .email(store.getEmail())
                .nickname(store.getNickname())
                .type(store.getType())
                .profileImage(store.getProfileImage())
                .build();
    }

    public static LoginResponse of(Customer customer) {
        return LoginResponse.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .nickname(customer.getNickname())
                .type(customer.getType())
                .profileImage(customer.getProfileImage())
                .build();
    }
}
