package com.restgram.domain.user.service.impl;

import com.restgram.domain.address.dto.response.AddressResponse;
import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.address.entity.SidoAddress;
import com.restgram.domain.address.entity.SiggAddress;
import com.restgram.domain.address.repository.EmdAddressRepository;
import com.restgram.domain.address.repository.SidoAddressRepository;
import com.restgram.domain.address.repository.SiggAddressRepository;
import com.restgram.domain.user.dto.request.UpdateCustomerRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.UserAddressListResponse;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.domain.user.service.CustomerService;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.AddressErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private final EmdAddressRepository emdAddressRepository;
  private final SiggAddressRepository siggAddressRepository;
  private final SidoAddressRepository sidoAddressRepository;

  @Override
  @Transactional(readOnly = true)
  public LoginResponse getUserInfo(Long userId) {
    Customer customer = customerRepository.findById(userId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID,
            "[일반] 사용자ID가 유효하지 않습니다. [사용자ID=" + userId + "]"));

    return LoginResponse.of(customer);
  }

  @Override
  @Transactional
  public void updateUserAddress(Long userId, Long addressId, Integer range) {
    Customer customer = customerRepository.findById(userId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
            "[일반] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));

    EmdAddress emdAddress = null;
    SiggAddress siggAddress = null;
    SidoAddress sidoAddress = null;
    // range가 0이면 시도도 전체
    switch (range) {
      case 1 -> // 시도 아이디 들어옴
          sidoAddress = sidoAddressRepository.findById(addressId)
              .orElseThrow(() -> new RestApiException(AddressErrorCode.INVALID_SIDO_ID,
                  "시도 ID가 유효하지 않습니다. [시도ID=" + addressId + "]"));
      case 2 -> {// 시군구 아이디 들어옴
        siggAddress = siggAddressRepository.findById(addressId)
            .orElseThrow(() -> new RestApiException(AddressErrorCode.INVALID_SIGG_ID,
                "시군구 ID가 유효하지 않습니다. [시군구ID=" + addressId + "]"));
        sidoAddress = siggAddress.getSidoAddress();
      }
      case 3 -> { // 읍면동 아이디 들어옴
        emdAddress = emdAddressRepository.findById(addressId)
            .orElseThrow(() -> new RestApiException(AddressErrorCode.INVALID_EMD_ID,
                "읍면동 ID가 유효하지 않습니다. [읍면동ID=" + addressId + "]"));
        siggAddress = emdAddress.getSiggAddress();
        sidoAddress = emdAddress.getSiggAddress().getSidoAddress();
      }
    }
    customer.updateAddress(emdAddress, siggAddress, sidoAddress, range);
  }

  @Override
  @Transactional
  public void updateCustomer(Long userId, UpdateCustomerRequest request) {
    Customer customer = customerRepository.findById(userId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
            "[일반] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));
    customer.updateDescription(request.getDescription());
    customer.updatePhone(request.getPhone());
  }

  @Override
  @Transactional(readOnly = true)
  public UserAddressListResponse getUserAddressList(Long userId) {
    Customer customer = customerRepository.findById(userId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
            "[일반] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));
    
    int range = customer.getAddressRange();

    // 시도는 항상 가져와야 함.(전체여도)
    List<AddressResponse> sidoResList = sidoAddressRepository.findAllByOrderByName().stream()
        .map(AddressResponse::of).collect(
            Collectors.toList());

    // 시군구는 시도가 전체가 아니라면 가져오기
    List<AddressResponse> siggResList = new ArrayList<>();
    if (range >= 1) {
      siggResList = siggAddressRepository.findALlBySidoAddressOrderByName(
          customer.getSidoAddress()).stream().map(AddressResponse::of).collect(Collectors.toList());
    }

    // 읍면동은 시군구가 전체가 아니라면 가져오기
    List<AddressResponse> emdResList = new ArrayList<>();
    if (range >= 2) {
      emdResList = emdAddressRepository.findAllBySiggAddressOrderByName(
          customer.getSiggAddress()).stream().map(AddressResponse::of).collect(Collectors.toList());
    }

    return UserAddressListResponse.builder()
        .emdAddressList(emdResList).siggAddressList(siggResList)
        .sidoAddressList(sidoResList).range(range)
        .sidoAddress(range >= 1 ? AddressResponse.of(customer.getSidoAddress()) : null)
        .siggAddress(range >= 2 ? AddressResponse.of(customer.getSiggAddress()) : null)
        .emdAddress(range >= 3 ? AddressResponse.of(customer.getEmdAddress()) : null)
        .build();
  }

}
