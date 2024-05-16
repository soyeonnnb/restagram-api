package com.restgram.domain.reservation.service;

import com.restgram.domain.reservation.dto.request.ReservationFormReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationFormServiceImpl implements ReservationFormService{
    @Override
    public void addReservationForm(Long id, ReservationFormReq req) {

    }
}
