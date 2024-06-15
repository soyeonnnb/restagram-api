package com.restgram.domain.address.repository;

import com.restgram.domain.address.entity.SidoAddress;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SidoAddressRepository extends JpaRepository<SidoAddress, Long> {
    List<SidoAddress> findAllByOrderByName();

    @EntityGraph(attributePaths = {"siggAddressList"})
    List<SidoAddress> findAll();
}
