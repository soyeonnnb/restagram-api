package com.restgram.domain.user.repository;

import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.CustomerCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerCalendarRepository extends JpaRepository<CustomerCalendar, Long> {

    boolean existsByCustomer(Customer customer);
}
