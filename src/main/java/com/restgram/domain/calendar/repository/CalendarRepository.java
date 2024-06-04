package com.restgram.domain.calendar.repository;

import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    boolean existsByCustomer(Customer customer);
    Optional<Calendar> findByCustomer(Customer customer);
}
