package cn.thoughtworks.school.programCenter.repositories;

import cn.thoughtworks.school.programCenter.entities.AssignmentQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AssignmentQuizRepository extends JpaRepository<AssignmentQuiz, Long> {
  List<AssignmentQuiz> findByAssignmentId(Long assignmentId);

  @Modifying
  @Transactional
  @Query(value = "DELETE  FROM  AssignmentQuiz b WHERE  b.assignmentId=:assignmentId")
  void  deleteAllByAssignmentId(@Param("assignmentId") Long assignmentId);

  @Modifying
  @Transactional
  @Query(value = "select Distinct(assignmentId) from AssignmentQuiz order by assignmentId")
  List<Long> findDistinctAssignmentId();

  List<AssignmentQuiz> findAllByAssignmentId(Long assignmentId);
}
