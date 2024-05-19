package com.restgram.domain.user.repository;

import com.restgram.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNickname(String nickname);

    @Query("SELECT u FROM User u WHERE u.name LIKE %:query% OR u.nickname LIKE %:query%")
    List<User> findAllByNicknameOrName(String query);
}
