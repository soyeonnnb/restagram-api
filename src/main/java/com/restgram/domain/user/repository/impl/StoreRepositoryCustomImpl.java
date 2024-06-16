package com.restgram.domain.user.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.repository.StoreRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.restgram.domain.user.entity.QStore.store;

@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Store> findByIdGreaterThanAndNicknameAndStoreNameLike(Long cursorId, String query) {
        BooleanBuilder builder = new BooleanBuilder();
        if (cursorId != null) {
            builder.and(store.id.gt(cursorId));
        }

        builder.and(store.nickname.startsWith(query).or(store.storeName.startsWith(query)));

        List<Store> storeList = queryFactory
                .selectFrom(store)
                .where(builder)
                .orderBy(store.id.asc())
                .limit(30)
                .fetch();


        return storeList;
    }
}
