package cn.thoughtworks.school.repositories;


import cn.thoughtworks.school.entities.UserQuizGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQuizGroupRepository extends JpaRepository<UserQuizGroup, Long> {
  List<UserQuizGroup> findByUserId(Long userId);

  UserQuizGroup findByUserIdAndQuizGroupId(Long userId, Long quizGroupId);

  Page<UserQuizGroup> findByQuizGroupId(Long quizGroupId, Pageable pageable);
}
