package com.restgram.domain.address.repository;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.address.entity.SiggAddress;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmdAddressRepository extends JpaRepository<EmdAddress, Long> {
    @EntityGraph(attributePaths = {"siggAddress", "siggAddress.sidoAddress"})
    Optional<EmdAddress> findById(Long id);

    List<EmdAddress> findAllBySiggAddressOrderByName(SiggAddress siggAddress);
}
