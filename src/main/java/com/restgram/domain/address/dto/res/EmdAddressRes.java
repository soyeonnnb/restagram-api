package com.restgram.domain.address.dto.res;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.address.entity.SiggAddress;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmdAddressRes {
    private Long id;
    private String emd_name;
    private String sigg_name;
    private String sido_name;

    public static EmdAddressRes of(EmdAddress emdAddress) {
        return EmdAddressRes.builder()
                .id(emdAddress.getId())
                .emd_name(emdAddress.getName())
                .sigg_name(emdAddress.getSiggAddress().getName())
                .sido_name(emdAddress.getSiggAddress().getSidoAddress().getName())
                .build();
    }
}
