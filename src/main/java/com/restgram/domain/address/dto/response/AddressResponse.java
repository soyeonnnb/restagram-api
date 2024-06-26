package com.restgram.domain.address.dto.response;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.address.entity.SidoAddress;
import com.restgram.domain.address.entity.SiggAddress;
import lombok.Builder;

@Builder
public record AddressResponse(

    Long id,
    String name
    
) {

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
