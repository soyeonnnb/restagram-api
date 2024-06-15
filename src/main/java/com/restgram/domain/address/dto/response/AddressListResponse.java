package com.restgram.domain.address.dto.response;

import com.restgram.domain.address.entity.SidoAddress;
import com.restgram.domain.address.entity.SiggAddress;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record AddressListResponse(
        Long id,
        String name,
        List<AddressList> list

) {

    public static AddressListResponse of(SidoAddress address) {
        return AddressListResponse.builder()
                .id(address.getId())
                .name(address.getName())
                .list(address.getSiggAddressList().stream().map(AddressList::of).collect(Collectors.toList()))
                .build();
    }

    @Builder
    record AddressList(
            Long id,
            String name,
            List<AddressResponse> list
    ) {
        public static AddressList of(SiggAddress address) {
            return AddressList.builder()
                    .id(address.getId())
                    .name(address.getName())
                    .list(address.getEmdAddressList().stream().map(AddressResponse::of).collect(Collectors.toList()))
                    .build();
        }
    }
}
