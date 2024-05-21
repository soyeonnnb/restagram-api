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
    private String emd_name;
    private String sigg_name;
    private String sido_name;

    public static EmdAddressResponse of(EmdAddress emdAddress) {
        return EmdAddressResponse.builder()
                .id(emdAddress.getId())
                .emd_name(emdAddress.getName())
                .sigg_name(emdAddress.getSiggAddress().getName())
                .sido_name(emdAddress.getSiggAddress().getSidoAddress().getName())
                .build();
    }
}
