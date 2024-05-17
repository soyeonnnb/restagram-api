package com.restgram.domain.user.repository;

import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.LoginMethod;
import com.restgram.domain.user.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUidAndLoginMethod(String uid, LoginMethod method);
    Optional<Customer> findByUid(String uid);
    Optional<Customer> findByEmailAndLoginMethod(String email, LoginMethod loginMethod);
    boolean existsByEmail(String email);
}
