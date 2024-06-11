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
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

  private final EmdAddressRepository emdAddressRepository;
  private final SiggAddressRepository siggAddressRepository;
  private final SidoAddressRepository sidoAddressRepository;

  @Override
  @Transactional(readOnly = true)
  public List<AddressResponse> getSidoList() {
    // 전체 시도 리스트 반환
    List<SidoAddress> sidoAddressList = sidoAddressRepository.findAllByOrderByName();

    return sidoAddressList.stream()
        .map(AddressResponse::of)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<AddressResponse> getSiggList(Long sidoId) {
    // 시도 엔티티를 이용해 시군구 리스트 반환
    SidoAddress sidoAddress = sidoAddressRepository.findById(sidoId).orElseThrow(
        () -> new RestApiException(AddressErrorCode.INVALID_SIDO_ID,
            "시도 ID가 유효하지 않습니다. [시도ID=" + sidoId + "]"));
    List<SiggAddress> siggAddressList = siggAddressRepository.findALlBySidoAddressOrderByName(
        sidoAddress);

    return siggAddressList.stream()
        .map(AddressResponse::of)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<AddressResponse> getEmdList(Long siggId) {
    // 시군구 엔티티를 이용해 시군구 리스트 반환
    SiggAddress siggAddress = siggAddressRepository.findById(siggId).orElseThrow(
        () -> new RestApiException(AddressErrorCode.INVALID_SIGG_ID,
            "시군구 ID가 유효하지 않습니다. [읍면동ID=" + siggId + "]"));
    List<EmdAddress> emdAddressList = emdAddressRepository.findAllBySiggAddressOrderByName(
        siggAddress);

    return emdAddressList.stream()
        .map(AddressResponse::of)
        .collect(Collectors.toList());
  }
}
