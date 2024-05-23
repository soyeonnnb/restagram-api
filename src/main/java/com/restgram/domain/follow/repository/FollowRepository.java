package com.restgram.domain.follow.repository;

import com.restgram.domain.follow.entity.Follow;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.Store;
import com.restgram.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteByFollowerAndFollowing(User follower, User following);
    List<Follow> findByFollowing(User following);
    List<Follow> findByFollower(User follower);
    boolean existsByFollowerAndFollowing(User follower, User following);

    @Query("SELECT f.following FROM Follow f WHERE f.follower = :follower")
    List<User> findFollowingsByFollower(@Param("follower") User follower);

    @Query("SELECT f.follower FROM Follow f WHERE f.following = :following")
    List<User> findFollowersByFollowing(@Param("following") User following);

    Integer countAllByFollowing(User user);
    Integer countAllByFollower(User user);
}
