package com.restgram.domain.user.service;

import com.restgram.domain.address.dto.res.AddressRes;
import com.restgram.domain.address.dto.res.EmdAddressRes;
import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.address.entity.SidoAddress;
import com.restgram.domain.address.entity.SiggAddress;
import com.restgram.domain.address.repository.EmdAddressRepository;
import com.restgram.domain.address.repository.SidoAddressRepository;
import com.restgram.domain.address.repository.SiggAddressRepository;
import com.restgram.domain.user.dto.request.CustomerJoinRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.UserAddressListResponse;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CommonErrorCode;
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
    public LoginResponse getUserInfo(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        LoginResponse res = LoginResponse.builder()
                .email(customer.getEmail())
                .nickname(customer.getNickname())
                .type(customer.getType())
                .build();
        return res;
    }

    @Override
    @Transactional
    public void join(CustomerJoinRequest req) {
        // 중복 이메일 확인
        if (customerRepository.existsByEmail(req.getEmail())) throw new RestApiException(UserErrorCode.EMAIL_DUPLICATED);

        // 중복 닉네임 확인
        if (userRepository.existsByNickname(req.getNickname())) throw new RestApiException(UserErrorCode.NICKNAME_DUPLICATED);

        // 비밀번호 암호화
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        // 저장
        customerRepository.save(req.of());
    }

    @Override
    @Transactional
    public void updateUserAddress(Long userId, Long addressId, Integer range) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        EmdAddress emdAddress = null;
        SiggAddress siggAddress = null;
        SidoAddress sidoAddress = null;
        // range가 0이면 시도도 전체
        switch (range) {
            case 1: // 시도 아이디 들어옴
                sidoAddress = sidoAddressRepository.findById(addressId).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND));
                break;
            case 2: // 시군구 아이디 들어옴
                siggAddress = siggAddressRepository.findById(addressId).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND));
                sidoAddress = siggAddress.getSidoAddress();
                break;
            case 3: // 읍면동 아이디 들어옴
                emdAddress = emdAddressRepository.findById(addressId).orElseThrow(() -> new RestApiException(CommonErrorCode.ENTITY_NOT_FOUND));
                siggAddress = emdAddress.getSiggAddress();
                sidoAddress = emdAddress.getSiggAddress().getSidoAddress();
                break;
        }
        customer.updateAddress(emdAddress, siggAddress, sidoAddress, range);
        customerRepository.save(customer);

    }

    @Override
    public UserAddressListResponse getUserAddressList(Long userId) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        int range = customer.getAddressRange();

        List<AddressRes> addressResList = new ArrayList<>();

        // 시도는 항상 가져와야 함.(전체여도)
        List<SidoAddress> sidoAddressList = sidoAddressRepository.findAllByOrderByName();
        List<AddressRes> sidoResList = new ArrayList<>();
        for(SidoAddress sidoAddress : sidoAddressList) sidoResList.add(AddressRes.of(sidoAddress));

        // 시군구는 시도가 전체가 아니라면 가져오기
        List<AddressRes> siggResList = new ArrayList<>();
        if (range >= 1) {
            List<SiggAddress> siggAddressList = siggAddressRepository.findALlBySidoAddressOrderByName(customer.getSidoAddress());
            for(SiggAddress siggAddress : siggAddressList) siggResList.add(AddressRes.of(siggAddress));
        }

        // 읍면동은 시군구가 전체가 아니라면 가져오기
        List<AddressRes> emdResList = new ArrayList<>();
        if (range >= 2) {
            List<EmdAddress> emdAddressList = emdAddressRepository.findAllBySiggAddressOrderByName(customer.getSiggAddress());
            for(EmdAddress emdAddress : emdAddressList) emdResList.add(AddressRes.of(emdAddress));
        }

        // 유저 초기 지역
        if (range >= 1) addressResList.add(AddressRes.of(customer.getSidoAddress()));
        if (range >= 2) addressResList.add(AddressRes.of(customer.getSiggAddress()));
        if (range >= 3) addressResList.add(AddressRes.of(customer.getEmdAddress()));

        UserAddressListResponse response = UserAddressListResponse.builder()
                .addressList(addressResList)
                .emdAddressList(emdResList)
                .siggAddressList(siggResList)
                .sidoAddressList(sidoResList)
                .range(range)
                .build();
        return response;
    }
}
