package com.restgram.domain.address.service;

import com.restgram.domain.address.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {
    List<AddressResponse> getSidoList();
    List<AddressResponse> getSiggList(Long sidoId);
    List<AddressResponse> getEmdList(Long siggId);
}
