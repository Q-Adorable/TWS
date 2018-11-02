package cn.thoughtworks.school.repositories;


import cn.thoughtworks.school.entities.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByFollowerId(Long followerId);

    Follow findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    List<Follow> findByFolloweeId(Long followeeId);
}
