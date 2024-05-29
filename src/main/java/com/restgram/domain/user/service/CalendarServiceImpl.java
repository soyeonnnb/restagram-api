package com.restgram.domain.user.service;

import com.restgram.domain.user.dto.request.CalendarAgreeRequest;
import com.restgram.domain.user.dto.request.CreateCalenderRequest;
import com.restgram.domain.user.dto.response.CalendarAgreeResponse;
import com.restgram.domain.user.dto.response.CreateCalendarResponse;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.repository.CustomerCalendarRepository;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final CustomerCalendarRepository customerCalendarRepository;
    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;
    private final CreateCalenderRequest createCalenderRequest;


    // 유저 캘린더 상태 변경
    @Override
    @Transactional
    public CalendarAgreeResponse customerCalendarAgree(Long userId, CalendarAgreeRequest request) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        // 만약 이전에 동의한 경험이 있다면
        if (customerCalendarRepository.existsByCustomer(customer)) {
            boolean agree = customer.updateCalendarAgree();
            customerRepository.save(customer);
            return CalendarAgreeResponse.builder()
                    .agree(agree)
                    .build();
        }

        // 이전에 동의한 경험이 없다면
        String calenderId = createCalender(customer);
        System.out.println("생성완료 : " + calenderId);


        return null;
    }

    // 카카오 API를 이용한 서브 캘린더 생성

    private String createCalender(Customer customer) {
        String url = "https://kapi.kakao.com/v2/api/calendar/create/calendar";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+customer.getAccessToken());

        HttpEntity<CreateCalenderRequest> requestEntity = new HttpEntity<>(createCalenderRequest, headers);

        ResponseEntity<CreateCalendarResponse> response = restTemplate.postForEntity(
                url,
                requestEntity,
                CreateCalendarResponse.class
        );

        String calenderId = response.getBody().getCalendar_id();
        System.out.println(calenderId);
        return calenderId;
    }

}
