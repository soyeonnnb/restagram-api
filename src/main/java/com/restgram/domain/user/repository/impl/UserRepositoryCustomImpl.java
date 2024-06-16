package com.restgram.domain.user.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.restgram.domain.user.entity.User;
import com.restgram.domain.user.repository.UserRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.restgram.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> findByIdGreaterThanAndNicknameLike(Long cursorId, String query) {
        List<User> userList = queryFactory
                .selectFrom(user)
                .where(user.id.gt(cursorId).and(user.nickname.startsWith(query)))
                .orderBy(user.id.asc())
                .limit(30)
                .fetch();

        return userList;
    }
}
