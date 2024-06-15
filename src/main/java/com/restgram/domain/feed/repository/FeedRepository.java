package com.restgram.domain.feed.repository;

import com.restgram.domain.feed.entity.Feed;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {

    @EntityGraph(attributePaths = {"feedImageList", "store", "store.emdAddress", "store.emdAddress.siggAddress", "store.emdAddress.siggAddress.sidoAddress"})
    Page<Feed> findAllByWriterInOrWriterOrderByIdDesc(List<User> userList, User writer, Pageable pageable);

    @EntityGraph(attributePaths = {"feedImageList", "store", "store.emdAddress", "store.emdAddress.siggAddress", "store.emdAddress.siggAddress.sidoAddress"})
    Page<Feed> findAllByWriterInOrderByIdDesc(List<User> followUserList, Pageable pageable);

    @EntityGraph(attributePaths = {"feedImageList", "store", "store.emdAddress", "store.emdAddress.siggAddress", "store.emdAddress.siggAddress.sidoAddress"})
    List<Feed> findByIdLessThanAndWriterInOrderByIdDesc(Long cursorId, List<User> userList, Pageable pageable);


    Integer countAllByWriter(User user);

    Integer countAllByStore(Store store);

    Optional<Feed> findTopByOrderByIdDesc();

}
