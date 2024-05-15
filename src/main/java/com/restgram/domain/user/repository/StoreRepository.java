package com.restgram.domain.user.repository;

import com.restgram.domain.user.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Store> findByEmail(String email);
}
