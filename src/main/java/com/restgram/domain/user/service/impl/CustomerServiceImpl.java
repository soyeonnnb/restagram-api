package com.restgram.domain.user.service.impl;

import com.restgram.domain.address.repository.EmdAddressRepository;
import com.restgram.domain.address.repository.SidoAddressRepository;
import com.restgram.domain.address.repository.SiggAddressRepository;
import com.restgram.domain.user.dto.request.UpdateCustomerRequest;
import com.restgram.domain.user.dto.response.LoginResponse;
import com.restgram.domain.user.dto.response.StoreInfoResponse;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.domain.user.service.CustomerService;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.UserErrorCode;
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
    private final StoreRepository storeRepository;

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
    public void updateCustomer(Long userId, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
                        "[일반] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));
        customer.updateDescription(request.description());
        customer.updatePhone(request.phone());
    }


    @Override
    public StoreInfoResponse getStoreInfo(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_USER_ID, "[가게] 사용자ID가 유효하지 않습니다. [사용자ID=" + storeId + "]"));
        return StoreInfoResponse.of(store);
    }

}
