package com.restgram.domain.user.dto.response;

import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.Store;
import lombok.Builder;

@Builder
public record LoginResponse(

    Long id,
    String email,
    String nickname,
    String type,
    String profileImage

) {

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
