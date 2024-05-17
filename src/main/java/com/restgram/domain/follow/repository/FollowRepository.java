package com.restgram.domain.follow.repository;

import com.restgram.domain.follow.entity.Follow;
import com.restgram.domain.user.entity.Customer;
import com.restgram.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteByFollowerAndFollowing(User follower, User following);
    List<Follow> findByFollowing(User following);
    List<Follow> findByFollower(User follower);
    boolean existsByFollowerAndFollowing(User follower, User following);
}
