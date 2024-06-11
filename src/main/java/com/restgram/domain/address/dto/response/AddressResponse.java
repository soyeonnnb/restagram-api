package com.restgram.domain.address.dto.response;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.address.entity.SidoAddress;
import com.restgram.domain.address.entity.SiggAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {

    private Long id;
    private String name;

    public static AddressResponse of(EmdAddress address) {
        return AddressResponse.builder()
                .id(address.getId())
                .name(address.getName())
                .build();
    }

    public static AddressResponse of(SiggAddress address) {
        return AddressResponse.builder()
                .id(address.getId())
                .name(address.getName())
                .build();
    }

    public static AddressResponse of(SidoAddress address) {
        return AddressResponse.builder()
                .id(address.getId())
                .name(address.getName())
                .build();
    }
}
