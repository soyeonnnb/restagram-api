package com.restgram.domain.reservation.service.impl;

import com.restgram.domain.calendar.repository.CalendarEventRepository;
import com.restgram.domain.calendar.service.CalendarEventService;
import com.restgram.domain.reservation.dto.request.AddReservationRequest;
import com.restgram.domain.reservation.dto.request.DeleteReservationRequest;
import com.restgram.domain.reservation.dto.response.CustomerReservationResponse;
import com.restgram.domain.reservation.dto.response.StoreReservationResponse;
import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.entity.ReservationCancel;
import com.restgram.domain.reservation.entity.ReservationCancelState;
import com.restgram.domain.reservation.entity.ReservationForm;
import com.restgram.domain.reservation.entity.ReservationFormState;
import com.restgram.domain.reservation.entity.ReservationState;
import com.restgram.domain.reservation.repository.ReservationCancelRepository;
import com.restgram.domain.reservation.repository.ReservationFormRepository;
import com.restgram.domain.reservation.repository.ReservationRepository;
import com.restgram.domain.reservation.service.ReservationService;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.repository.CustomerRepository;
import com.restgram.domain.user.repository.StoreRepository;
import com.restgram.domain.user.repository.UserRepository;
import com.restgram.global.exception.entity.RestApiException;
import com.restgram.global.exception.errorCode.ReservationErrorCode;
import com.restgram.global.exception.errorCode.UserErrorCode;
import com.restgram.global.sse.entity.NotificationType;
import com.restgram.global.sse.service.NotificationService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

  private final UserRepository userRepository;
  private final CustomerRepository customerRepository;
  private final ReservationFormRepository reservationFormRepository;
  private final ReservationRepository reservationRepository;
  private final ReservationCancelRepository reservationCancelRepository;
  private final StoreRepository storeRepository;
  private final NotificationService notificationService;
  private final CalendarEventService calendarEventService;
  private final CalendarEventRepository calendarEventRepository;

  @Override
  @Transactional
  public void addReservation(Long userId, AddReservationRequest request) {
    Customer customer = customerRepository.findById(userId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
            "[일반] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));

    ReservationForm form = reservationFormRepository.findById(request.reservationFormId())
        .orElseThrow(() -> new RestApiException(ReservationErrorCode.INVALID_RESERVATION_FORM_ID,
            "예약폼ID가 유효하지 않습니다. [예약폼ID=" + request.reservationFormId() + "]"));

    // 예약 일자 확인
    LocalDateTime formDate = LocalDateTime.of(form.getDate(), form.getTime());

    if (formDate.isBefore(LocalDateTime.now())) {
      throw new RestApiException(ReservationErrorCode.RESERVATION_IS_BEFORE_NOW,
          "이전 날짜는 예약할 수 없습니다. [예약폼ID=" + request.reservationFormId() + ", 예약폼 날짜=" + formDate
              + "]");
    }
    if (form.getState().toString().equals(ReservationFormState.DISABLE)) {
      throw new RestApiException(ReservationErrorCode.RESERVATION_DISABLE,
          "예약이 불가능한 상태입니다. [예약폼ID=" + request.reservationFormId() + "]");
    }
    // 테이블 수 확인
    int tableNum = getTableNum(request.headCount(), form.getTablePerson());
    if (form.getRemainQuantity() < tableNum) {
      throw new RestApiException(ReservationErrorCode.TABLE_UNAVAILABLE,
          "테이블 수가 부족합니다. [예약폼ID=" + request.reservationFormId() + ", 남은 테이블 수="
              + form.getRemainQuantity() + ", 요청 테이블 수=" + tableNum + "]");
    }
    // 예약 가능 수 확인
    if (form.getMaxReservationPerson() < request.headCount()) {
      throw new RestApiException(ReservationErrorCode.RESERVATION_HEAD_COUNT_EXCEED_TABLE_NUM,
          "최대 예약 인원수를 초과하였습니다. [예약폼ID=" + request.reservationFormId() + ", 최대 예약 인원수="
              + form.getMaxReservationPerson() + ", 요청 예약 인원수=" + request.headCount() + "]");
    }

    // 예약 생성
    Reservation reservation = request.toEntity(form, form.getStore(), customer);
    reservationRepository.save(reservation);
    form.updateRemainQuantity(tableNum * -1);

    // 예약 생성 알림 보내기
    notificationService.send(reservation.getStore(), NotificationType.NEW_RESERVATION, reservation);

    // 만약 캘린더 동의가 되어 있다면 일정에 추가하기
    if (customer.isCalendarAgree()) {
      calendarEventService.createCalendarEvent(reservation);
    }

  }

  @Override
  @Transactional
  public void cancelReservation(Long userId, DeleteReservationRequest request) {
    Reservation reservation = reservationRepository.findById(request.reservationId())
        .orElseThrow(() -> new RestApiException(ReservationErrorCode.INVALID_RESERVATION_ID,
            "예약ID가 유효하지 않습니다. [예약ID=" + request.reservationId() + "]"));
    // 만약 해당 유저와 관계 없는 예약이면 삭제 취소 불가
    if ((request.state().equals(ReservationCancelState.CUSTOMER)
        && reservation.getCustomer().getId() != userId) || (
        request.state().equals(ReservationCancelState.STORE)
            && reservation.getStore().getId() != userId)) {
      throw new RestApiException(UserErrorCode.USER_MISMATCH,
          "로그인 사용자와 관계 없는 예약입니다. [로그인 사용자ID=" + userId + ", 예약ID=" + request.reservationId()
              + "]");
    }

    // 이미 취소처리되었거나 기간이 지났으면 예약이면 취소 불가
    if (!reservation.getState().equals(ReservationState.ACTIVE) || reservation.getDatetime()
        .isBefore(LocalDateTime.now())) {
      throw new RestApiException(ReservationErrorCode.RESERVATION_CANCEL_DISABLE,
          "취소가능한 상태가 아닙니다. [예약ID=" + request.reservationId() + "]");
    }

    ReservationCancel reservationCancel = request.toEntity(reservation);

    // 삭제 처리
    if (request.state().equals(ReservationCancelState.CUSTOMER)) {
      reservation.updateState(ReservationState.USER_CANCELED);
    } else if (request.state().equals(ReservationCancelState.STORE)) {
      reservation.updateState(ReservationState.STORE_CANCELED);
    }

    // 테이블 수 업데이트
    reservation.getReservationForm().updateRemainQuantity(
        getTableNum(reservation.getHeadCount(), reservation.getReservationForm().getTablePerson()));

    // 엔티티 저장
    reservationCancelRepository.save(reservationCancel);

    // 알림 보내기
    if (request.state().equals(ReservationCancelState.CUSTOMER)) {
      notificationService.send(reservation.getStore(), NotificationType.CUSTOMER_RESERVATION_CANCEL,
          reservation);
    } else {
      notificationService.send(reservation.getCustomer(), NotificationType.STORE_RESERVATION_CANCEL,
          reservation);
    }

    // 카카오 일정 삭제
    if (calendarEventRepository.existsByReservation(reservation)) {
      calendarEventService.deleteCalendarEvent(reservation);
    }
  }

  // 구매자 예약 리스트
  @Override
  @Transactional(readOnly = true)
  public List<CustomerReservationResponse> getCustomerReservations(Long userId) {
    Customer customer = customerRepository.findById(userId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
            "[일반] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));

    List<Reservation> reservationList = reservationRepository.findAllByCustomerOrderByDatetimeDesc(
        customer);

    return reservationList.stream()
        .map(reservation -> {
          if (reservation.getState().equals(ReservationState.ACTIVE)) { // 예약이 정상 상태
            return CustomerReservationResponse.of(reservation);
          } else { // 예약이 취소상태일 시 취소 메세지도 함께보냄
            ReservationCancel reservationCancel = reservationCancelRepository.findByReservation(
                    reservation)
                .orElseThrow(
                    () -> new RestApiException(ReservationErrorCode.CANNOT_FIND_RESERVATION_CANCEL,
                        "예약 취소 데이터를 찾을 수 없습니다. [예약ID=" + reservation.getId() + "]"));
            return CustomerReservationResponse.of(reservation, reservationCancel.getMemo());
          }
        })
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<StoreReservationResponse> getStoreReservations(Long userId, Integer year,
      Integer month) {
    Store store = storeRepository.findById(userId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_LOGIN_USER_ID,
            "[가게] 로그인 사용자ID가 유효하지 않습니다. [로그인 사용자ID=" + userId + "]"));

    LocalDateTime startAt = LocalDateTime.of(year, month, 1, 0, 0);
    // 해당 가게의, 연도/월 검색
    List<Reservation> reservationList = reservationRepository.findAllByStoreAndDatetimeGreaterThanEqualAndDatetimeLessThanOrderByDatetime(
        store, startAt, startAt.plusMonths(1));

    return reservationList.stream()
        .map(reservation -> {
          if (reservation.getState().equals(ReservationState.ACTIVE)) {// 예약이 정상 상태
            return StoreReservationResponse.of(reservation);
          } else { // 예약이 취소상태일 시 취소 메세지도 함께보냄
            ReservationCancel reservationCancel = reservationCancelRepository.findByReservation(
                    reservation)
                .orElseThrow(
                    () -> new RestApiException(ReservationErrorCode.CANNOT_FIND_RESERVATION_CANCEL,
                        "예약 취소 데이터를 찾을 수 없습니다. [예약ID=" + reservation.getId() + "]"));
            return StoreReservationResponse.of(reservation, reservationCancel.getMemo());
          }
        })
        .collect(Collectors.toList());
  }

  private int getTableNum(Integer headcount, Integer tablePerson) {
    Integer table = headcount / tablePerson;
    if (headcount % tablePerson != 0) {
      table++;
    }
    return table;
  }
}
