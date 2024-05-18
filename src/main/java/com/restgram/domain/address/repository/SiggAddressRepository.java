package com.restgram.domain.address.repository;

import com.restgram.domain.address.entity.SidoAddress;
import com.restgram.domain.address.entity.SiggAddress;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SiggAddressRepository extends JpaRepository<SiggAddress, Long> {
    List<SiggAddress> findALlBySidoAddressOrderByName(SidoAddress sidoAddress);

    @EntityGraph(attributePaths = "sidoAddress")
    Optional<SiggAddress> findById(Long id);
}
