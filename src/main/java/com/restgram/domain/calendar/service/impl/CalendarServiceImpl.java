package com.restgram.domain.calendar.service.impl;

import com.restgram.domain.calendar.dto.request.CalendarAgreeRequest;
import com.restgram.domain.calendar.service.CalendarService;
import com.restgram.domain.calendar.dto.response.CalendarAgreeResponse;
import com.restgram.domain.calendar.dto.response.CreateCalendarResponse;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.calendar.entity.Calendar;
import com.restgram.domain.calendar.repository.CalendarRepository;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CalendarErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository calendarRepository;
    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    // 유저 캘린더 상태 변경
    @Override
    @Transactional
    public CalendarAgreeResponse agreeCalendar(Long userId, CalendarAgreeRequest request) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID, "[일반] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID="+userId+"]"));

        // 이전에 동의한 경험이 없다면 생성 불가
        if (!calendarRepository.existsByCustomer(customer)) throw new RestApiException(CalendarErrorCode.CALENDER_NOT_AUTHORIZATION, "카카오 캘린더에 동의하지 않았습니다. [회원ID="+customer.getId()+"]");

        boolean isAgree = customer.updateCalendarAgree(request.isAgree());
        return CalendarAgreeResponse.builder()
                .agree(isAgree)
                .build();
    }

    @Override
    @Transactional
    public void createCalendar(Customer customer) {
        // 캘린더 생성하기
        String calenderId = requestCreateCalender(customer);
        calendarRepository.save(Calendar.builder()
                .customer(customer)
                .calendarId(calenderId)
                .build());
    }


    // 카카오 API를 이용한 서브 캘린더 생성
    private String requestCreateCalender(Customer customer) {
        String url = "https://kapi.kakao.com/v2/api/calendar/create/calendar";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + customer.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("name", "Restagram");
        map.add("color", "MINT");
        map.add("reminder", "60"); // 일정 60분 전 알림

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<CreateCalendarResponse> response = restTemplate.postForEntity(
                url,
                requestEntity,
                CreateCalendarResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            String calenderId = response.getBody().getCalendar_id();
            return calenderId;
        } else {
            throw new RuntimeException("카카오 캘린더 생성에 실패했습니다. : " + response.getStatusCode());
        }
    }

}
