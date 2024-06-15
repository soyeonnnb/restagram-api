package com.restgram.domain.user.repository;

import com.restgram.domain.user.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> findByIdGreaterThanAndNicknameLike(Long cursorId, String query);
}
