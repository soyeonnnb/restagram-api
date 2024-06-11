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
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.domain.user.service.CustomerService;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.AddressErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmdAddressRepository emdAddressRepository;
    private final SiggAddressRepository siggAddressRepository;
    private final SidoAddressRepository sidoAddressRepository;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse getUserInfo(Long userId) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        LoginResponse res = LoginResponse.of(customer);
        return res;
    }

    @Override
    @Transactional
    public void updateUserAddress(Long userId, Long addressId, Integer range) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        EmdAddress emdAddress = null;
        SiggAddress siggAddress = null;
        SidoAddress sidoAddress = null;
        // range가 0이면 시도도 전체
        switch (range) {
            case 1: // 시도 아이디 들어옴
                sidoAddress = sidoAddressRepository.findById(addressId).orElseThrow(() -> new RestApiException(AddressErrorCode.INVALID_SIDO_ID));
                break;
            case 2: // 시군구 아이디 들어옴
                siggAddress = siggAddressRepository.findById(addressId).orElseThrow(() -> new RestApiException(AddressErrorCode.INVALID_SIGG_ID));
                sidoAddress = siggAddress.getSidoAddress();
                break;
            case 3: // 읍면동 아이디 들어옴
                emdAddress = emdAddressRepository.findById(addressId).orElseThrow(() -> new RestApiException(AddressErrorCode.INVALID_EMD_ID));
                siggAddress = emdAddress.getSiggAddress();
                sidoAddress = emdAddress.getSiggAddress().getSidoAddress();
                break;
        }
        customer.updateAddress(emdAddress, siggAddress, sidoAddress, range);
        customerRepository.save(customer);

    }

    @Override
    @Transactional
    public void updateCustomer(Long userId, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        customer.updateDescription(request.getDescription());
        customer.updatePhone(request.getPhone());
        customerRepository.save(customer);
    }

    @Override
    public UserAddressListResponse getUserAddressList(Long userId) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID));
        int range = customer.getAddressRange();

        List<AddressResponse> addressResponseList = new ArrayList<>();

        // 시도는 항상 가져와야 함.(전체여도)
        List<SidoAddress> sidoAddressList = sidoAddressRepository.findAllByOrderByName();
        List<AddressResponse> sidoResList = new ArrayList<>();
        for(SidoAddress sidoAddress : sidoAddressList) sidoResList.add(AddressResponse.of(sidoAddress));

        // 시군구는 시도가 전체가 아니라면 가져오기
        List<AddressResponse> siggResList = new ArrayList<>();
        if (range >= 1) {
            List<SiggAddress> siggAddressList = siggAddressRepository.findALlBySidoAddressOrderByName(customer.getSidoAddress());
            for(SiggAddress siggAddress : siggAddressList) siggResList.add(AddressResponse.of(siggAddress));
        }

        // 읍면동은 시군구가 전체가 아니라면 가져오기
        List<AddressResponse> emdResList = new ArrayList<>();
        if (range >= 2) {
            List<EmdAddress> emdAddressList = emdAddressRepository.findAllBySiggAddressOrderByName(customer.getSiggAddress());
            for(EmdAddress emdAddress : emdAddressList) emdResList.add(AddressResponse.of(emdAddress));
        }

        // 유저 초기 지역
        if (range >= 1) addressResponseList.add(AddressResponse.of(customer.getSidoAddress()));
        if (range >= 2) addressResponseList.add(AddressResponse.of(customer.getSiggAddress()));
        if (range >= 3) addressResponseList.add(AddressResponse.of(customer.getEmdAddress()));

        UserAddressListResponse response = UserAddressListResponse.builder()
                .addressList(addressResponseList)
                .emdAddressList(emdResList)
                .siggAddressList(siggResList)
                .sidoAddressList(sidoResList)
                .range(range)
                .build();
        return response;
    }

}
