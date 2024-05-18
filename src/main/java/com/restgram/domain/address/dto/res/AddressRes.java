package com.restgram.domain.address.dto.res;

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
public class AddressRes {
    private Long id;
    private String name;

    public static AddressRes of(EmdAddress address) {
        return AddressRes.builder()
                .id(address.getId())
                .name(address.getName())
                .build();
    }

    public static AddressRes of(SiggAddress address) {
        return AddressRes.builder()
                .id(address.getId())
                .name(address.getName())
                .build();
    }

    public static AddressRes of(SidoAddress address) {
        return AddressRes.builder()
                .id(address.getId())
                .name(address.getName())
                .build();
    }
}
