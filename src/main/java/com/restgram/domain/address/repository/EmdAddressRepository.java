package com.restgram.domain.address.repository;

import com.restgram.domain.address.entity.EmdAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmdAddressRepository extends JpaRepository<EmdAddress, Long> {
    Optional<EmdAddress> findById(Long id);
}
