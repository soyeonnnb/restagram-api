package com.restgram.domain.user.dto.response;

import com.restgram.domain.user.entity.Store;
import lombok.Builder;

@Builder
public record StoreInfoResponse(

        Long id,
        String profileImage,
        String nickname,
        String storeName,
        String address,
        String detailAddress,
        String storePhone,
        Integer maxReservationPerson,
        Integer tablePerson

) {

    public static StoreInfoResponse of(Store store) {
        return StoreInfoResponse.builder()
                .id(store.getId())
                .profileImage(store.getProfileImage())
                .nickname(store.getNickname())
                .storeName(store.getStoreName())
                .address(store.getAddress())
                .detailAddress(store.getDetailAddress())
                .storePhone(store.getStorePhone())
                .maxReservationPerson(store.getMaxReservationPerson())
                .tablePerson(store.getTablePerson())
                .build();
    }
}
