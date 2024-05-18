package com.restgram.domain.user.repository;

import com.restgram.domain.user.entity.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Store> findByEmail(String email);

    @Query("SELECT s FROM Store s WHERE s.storeName LIKE %:param% OR s.nickname LIKE %:param%")
    @EntityGraph(attributePaths = {"address", "address.siggAddress", "address.siggAddress.sidoAddress"})
    List<Store> findAllByStoreNameOrNicknameContaining(@Param("param") String parameter);
}
