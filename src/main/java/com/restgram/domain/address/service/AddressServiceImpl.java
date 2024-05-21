package com.restgram.domain.address.service;

import com.restgram.domain.address.dto.response.AddressResponse;
import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.address.entity.SidoAddress;
import com.restgram.domain.address.entity.SiggAddress;
import com.restgram.domain.address.repository.EmdAddressRepository;
import com.restgram.domain.address.repository.SidoAddressRepository;
import com.restgram.domain.address.repository.SiggAddressRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CommonErrorCode;
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
        SidoAddress sidoAddress = sidoAddressRepository.findById(sidoId).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND));
        List<SiggAddress> siggAddressList = siggAddressRepository.findALlBySidoAddressOrderByName(sidoAddress);
        List<AddressResponse> addressResponseList = new ArrayList<>();
        for(SiggAddress siggAddress : siggAddressList) {
            addressResponseList.add(AddressResponse.of(siggAddress));
        }
        return addressResponseList;
    }

    @Override
    public List<AddressResponse> getEmdList(Long siggId) {
        SiggAddress siggAddress = siggAddressRepository.findById(siggId).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND));
        List<EmdAddress> emdAddressList = emdAddressRepository.findAllBySiggAddressOrderByName(siggAddress);
        List<AddressResponse> addressResponseList = new ArrayList<>();
        for(EmdAddress emdAddress : emdAddressList) {
            addressResponseList.add(AddressResponse.of(emdAddress));
        }
        return addressResponseList;
    }
}
