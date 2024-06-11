package com.restgram.domain.address.dto.response;

import com.restgram.domain.address.entity.EmdAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmdAddressResponse {
    private Long id;
    private String emdName;
    private String siggName;
    private String sidoName;

    public static EmdAddressResponse of(EmdAddress emdAddress) {
        return EmdAddressResponse.builder()
                .id(emdAddress.getId())
                .emdName(emdAddress.getName())
                .siggName(emdAddress.getSiggAddress().getName())
                .sidoName(emdAddress.getSiggAddress().getSidoAddress().getName())
                .build();
    }
}
