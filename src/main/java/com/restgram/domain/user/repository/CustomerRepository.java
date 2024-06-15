package com.restgram.domain.user.repository;

import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.LoginMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUidAndLoginMethod(String uid, LoginMethod method);

    boolean existsByEmail(String email);

    Optional<Customer> findById(Long id);
}
