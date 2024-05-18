package com.restgram.domain.address.service;

import com.restgram.domain.address.dto.res.AddressRes;

import java.util.List;

public interface AddressService {
    List<AddressRes> getSidoList();
    List<AddressRes> getSiggList(Long sidoId);
    List<AddressRes> getEmdList(Long siggId);
}
