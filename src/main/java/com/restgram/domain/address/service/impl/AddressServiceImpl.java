package com.restgram.domain.address.service.impl;

import com.restgram.domain.address.dto.response.AddressResponse;
import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.address.entity.SidoAddress;
import com.restgram.domain.address.entity.SiggAddress;
import com.restgram.domain.address.repository.EmdAddressRepository;
import com.restgram.domain.address.repository.SidoAddressRepository;
import com.restgram.domain.address.repository.SiggAddressRepository;
import com.restgram.domain.address.service.AddressService;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.AddressErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final EmdAddressRepository emdAddressRepository;
    private final SiggAddressRepository siggAddressRepository;
    private final SidoAddressRepository sidoAddressRepository;

    @Override
    public List<AddressResponse> getSidoList() {
        List<SidoAddress> sidoAddressList = sidoAddressRepository.findAllByOrderByName();
        List<AddressResponse> addressResponseList = new ArrayList<>();
        for(SidoAddress sidoAddress : sidoAddressList) {
            addressResponseList.add(AddressResponse.of(sidoAddress));
        }
        return addressResponseList;
    }

    @Override
    public List<AddressResponse> getSiggList(Long sidoId) {
        SidoAddress sidoAddress = sidoAddressRepository.findById(sidoId).orElseThrow(() -> new RestApiException(AddressErrorCode.INVALID_SIDO_ID, "시도 ID가 유효하지 않습니다. [ID="+sidoId+"]"));
        List<SiggAddress> siggAddressList = siggAddressRepository.findALlBySidoAddressOrderByName(sidoAddress);
        List<AddressResponse> addressResponseList = new ArrayList<>();
        for(SiggAddress siggAddress : siggAddressList) {
            addressResponseList.add(AddressResponse.of(siggAddress));
        }
        return addressResponseList;
    }

    @Override
    public List<AddressResponse> getEmdList(Long siggId) {
        SiggAddress siggAddress = siggAddressRepository.findById(siggId).orElseThrow(() -> new RestApiException(AddressErrorCode.INVALID_SIGG_ID, "시군구 ID가 유효하지 않습니다. [ID="+siggId+"]"));
        List<EmdAddress> emdAddressList = emdAddressRepository.findAllBySiggAddressOrderByName(siggAddress);
        List<AddressResponse> addressResponseList = new ArrayList<>();
        for(EmdAddress emdAddress : emdAddressList) {
            addressResponseList.add(AddressResponse.of(emdAddress));
        }
        return addressResponseList;
    }
}
