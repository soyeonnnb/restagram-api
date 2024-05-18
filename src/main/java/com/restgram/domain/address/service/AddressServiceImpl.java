package com.restgram.domain.address.service;

import com.amazonaws.services.ec2.model.Address;
import com.restgram.domain.address.dto.res.AddressRes;
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
    public List<AddressRes> getSidoList() {
        List<SidoAddress> sidoAddressList = sidoAddressRepository.findAllByOrderByName();
        List<AddressRes> addressResList = new ArrayList<>();
        for(SidoAddress sidoAddress : sidoAddressList) {
            addressResList.add(AddressRes.of(sidoAddress));
        }
        return addressResList;
    }

    @Override
    public List<AddressRes> getSiggList(Long sidoId) {
        SidoAddress sidoAddress = sidoAddressRepository.findById(sidoId).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND));
        List<SiggAddress> siggAddressList = siggAddressRepository.findALlBySidoAddressOrderByName(sidoAddress);
        List<AddressRes> addressResList = new ArrayList<>();
        for(SiggAddress siggAddress : siggAddressList) {
            addressResList.add(AddressRes.of(siggAddress));
        }
        return addressResList;
    }

    @Override
    public List<AddressRes> getEmdList(Long siggId) {
        SiggAddress siggAddress = siggAddressRepository.findById(siggId).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND));
        List<EmdAddress> emdAddressList = emdAddressRepository.findAllBySiggAddressOrderByName(siggAddress);
        List<AddressRes> addressResList = new ArrayList<>();
        for(EmdAddress emdAddress : emdAddressList) {
            addressResList.add(AddressRes.of(emdAddress));
        }
        return addressResList;
    }
}
