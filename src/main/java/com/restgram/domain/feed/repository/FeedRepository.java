package com.restgram.domain.feed.repository;

import com.restgram.domain.address.entity.EmdAddress;
import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    @EntityGraph(attributePaths = {"feedImageList", "store", "store.emdAddress", "store.emdAddress.siggAddress", "store.emdAddress.siggAddress.sidoAddress"})
    Page<Feed> findAllByWriterInOrWriterOrderByIdDesc(List<User> userList, User writer, Pageable pageable);

    @EntityGraph(attributePaths = {"feedImageList", "store", "store.emdAddress", "store.emdAddress.siggAddress", "store.emdAddress.siggAddress.sidoAddress"})
    Page<Feed> findAllByWriterInOrderByIdDesc(List<User> followUserList, Pageable pageable);

    @EntityGraph(attributePaths = {"feedImageList", "store", "store.emdAddress", "store.emdAddress.siggAddress", "store.emdAddress.siggAddress.sidoAddress"})
    List<Feed> findByIdLessThanAndWriterInOrderByIdDesc(Long cursorId, List<User> userList, Pageable pageable);

    @EntityGraph(attributePaths = {"store", "store.emdAddress", "store.emdAddress.siggAddress", "store.emdAddress.siggAddress.sidoAddress"})
    @Query("select f from Feed f where f.store.nickname LIKE %:query% or f.store.storeName LIKE %:query% or f.content LIKE %:query%")
    List<Feed> searchByQuery(String query, Pageable pageable);

    @EntityGraph(attributePaths = {"store", "store.emdAddress", "store.emdAddress.siggAddress", "store.emdAddress.siggAddress.sidoAddress"})
    @Query("select f from Feed f where (f.store.nickname LIKE %:query% or f.store.storeName LIKE %:query% or f.content LIKE %:query%) and f.store.emdAddress in :emdAddressList")
    List<Feed> searchByQueryAndEmdAddressList(String query, List<EmdAddress> emdAddressList, Pageable pageable);

    Integer countAllByWriter(User user);
    Integer countAllByStore(Store store);

    Optional<Feed> findTopByOrderByIdDesc();

}
