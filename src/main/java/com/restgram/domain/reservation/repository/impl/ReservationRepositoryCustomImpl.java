package com.restgram.domain.reservation.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.restgram.domain.reservation.entity.Reservation;
import com.restgram.domain.reservation.repository.ReservationRepositoryCustom;
import com.restgram.domain.user.entity.Customer;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.restgram.domain.reservation.entity.QReservation.reservation;

@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Reservation> findAllByCustomerAndDatetimeBeforeThanOrderByDatetimeDesc(Customer loginUser, Long cursorId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(reservation.customer.eq(loginUser));

        if (cursorId != null)
            builder.and(reservation.datetime.before(queryFactory.selectFrom(reservation).where(reservation.id.eq(cursorId)).fetchFirst().getDatetime()));

        List<Reservation> limitList = queryFactory.selectFrom(reservation)
                .where(builder)
                .orderBy(reservation.datetime.desc())
                .limit(20)
                .fetch();

        List<Long> ids = limitList.stream().map(Reservation::getId).collect(Collectors.toList());

        List<Reservation> reservationList = queryFactory.selectFrom(reservation)
                .where(reservation.id.in(ids))
                .join(reservation.store)
                .fetch();

        return reservationList;
    }
}
