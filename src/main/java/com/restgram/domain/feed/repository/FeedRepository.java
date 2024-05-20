package com.restgram.domain.feed.repository;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    @EntityGraph(attributePaths = {"store", "store.emdAddress", "store.emdAddress.siggAddress", "store.emdAddress.siggAddress.sidoAddress"})
    List<Feed> findAllByWriterInOrWriterOrderByCreatedAtDesc(List<User> userList, User writer);

    @EntityGraph(attributePaths = {"store", "store.emdAddress", "store.emdAddress.siggAddress", "store.emdAddress.siggAddress.sidoAddress"})
    @Query("select f from Feed f where f.store.nickname LIKE %:query% or f.store.storeName LIKE %:query% or f.content LIKE %:query%")
    List<Feed> searchByQuery(String query);

    @EntityGraph(attributePaths = {"store", "store.emdAddress", "store.emdAddress.siggAddress", "store.emdAddress.siggAddress.sidoAddress"})
    @Query("select f from Feed f where (f.store.nickname LIKE %:query% or f.store.storeName LIKE %:query% or f.content LIKE %:query%) and f.store.emdAddress in :emdAddressList")
    List<Feed> searchByQueryAndEmdAddressList(String query, List<EmdAddress> emdAddressList);

    Integer countAllByWriter(User user);
    Integer countAllByStore(Store store);
}
