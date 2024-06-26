package com.restgram.domain.calendar.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restgram.domain.calendar.dto.request.EventCreate;
import com.restgram.domain.calendar.dto.response.CalendarEventResponse;
import com.restgram.domain.calendar.entity.CalendarEvent;
import com.restgram.domain.calendar.repository.CalendarEventRepository;
import com.restgram.domain.calendar.repository.CalendarRepository;
import com.restgram.domain.calendar.service.CalendarEventService;
import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.user.entity.Customer;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.CalendarErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarEventServiceImpl implements CalendarEventService {

  private final CalendarRepository calendarRepository;
  private final CalendarEventRepository calendarEventRepository;
  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;  // entity -> json

  @Override
  @Transactional
  @Async("kakaoAsyncExecutor")
  public void createCalendarEvent(Reservation reservation) {
    String calendarId = calendarRepository.findByCustomer(reservation.getCustomer())
        .orElseThrow(() -> new RestApiException(CalendarErrorCode.CALENDER_NOT_AUTHORIZATION,
            "카카오 캘린더에 동의하지 않았습니다. [사용자ID=" + reservation.getCustomer().getId() + "]"))
        .getCalendarId();

    // 일정 생성하기
    String eventId = requestCreateCalenderEvent(reservation, calendarId);
    log.info("사용자 일정 생성완료 : " + eventId);
    calendarEventRepository.save(
        CalendarEvent.builder()
            .reservation(reservation)
            .eventId(eventId)
            .build()
    );
  }


  // 카카오 API를 이용한 일정 생성
  private String requestCreateCalenderEvent(Reservation reservation, String calendarId) {
    String url = "https://kapi.kakao.com/v2/api/calendar/create/event";

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + reservation.getCustomer().getAccessToken());
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    EventCreate eventCreate = EventCreate.of(reservation);

    String eventJson;
    try {
      eventJson = objectMapper.writeValueAsString(eventCreate);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("EventCreate를 JSON로 변환하는 데 실패했습니다.", e);
    }

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("event", eventJson);
    map.add("calendar_id", calendarId);

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

    ResponseEntity<CalendarEventResponse> response = restTemplate.postForEntity(
        url,
        requestEntity,
        CalendarEventResponse.class
    );

    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
      String eventId = response.getBody().event_id();
      return eventId;
    } else {
      throw new RuntimeException("카카오 일정 생성에 실패했습니다. : " + response.getStatusCode());
    }
  }


  @Override
  @Transactional
  @Async("kakaoAsyncExecutor")
  public void deleteCalendarEvent(Reservation reservation) {
    CalendarEvent calendarEvent = calendarEventRepository
        .findByReservation(reservation)
        .orElseThrow(() -> new RestApiException(CalendarErrorCode.CANNOT_FIND_CALENDAR_EVENT,
            "예약에 대한 카카오 일정을 찾을 수 없습니다. [예약ID=" + reservation.getId() + "]"));

    // 일정 삭제하기
    requestDeleteCalenderEvent(reservation.getCustomer(), calendarEvent.getEventId());
    log.info("사용자 카카오 일정 삭제완료 : " + calendarEvent.getEventId());
    calendarEventRepository.delete(calendarEvent);
  }

  // 카카오 API를 이용한 일정 삭제
  private boolean requestDeleteCalenderEvent(Customer customer, String eventId) {
    String url = "https://kapi.kakao.com/v2/api/calendar/delete/event?event_id=" + eventId;

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + customer.getAccessToken());
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    ResponseEntity<CalendarEventResponse> response = restTemplate.exchange(
        url,
        HttpMethod.DELETE,
        requestEntity,
        CalendarEventResponse.class
    );

    if (response.getStatusCode().is2xxSuccessful()) {
      return true;
    } else {
      throw new RuntimeException("카카오 일정 삭제에 실패했습니다. : " + response.getStatusCode());
    }
  }
}
