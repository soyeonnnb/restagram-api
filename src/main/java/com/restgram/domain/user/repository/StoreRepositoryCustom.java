package com.restgram.domain.user.repository;

import com.restgram.domain.user.entity.Store;

import java.util.List;

public interface StoreRepositoryCustom {
    List<Store> findByIdGreaterThanAndNicknameAndStoreNameLike(Long cursorId, String query);
}
