package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.BasicQuizSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;


public interface BasicQuizSubmissionRepository extends JpaRepository<BasicQuizSubmission,Long>{

  List<BasicQuizSubmission> findByQuizIdAndAssignmentIdOrderBySubmitTimeDesc(Long quizId, Long assignmentId);
  ArrayList<BasicQuizSubmission> findByQuizIdAndAssignmentIdAndUserIdOrderByIdDesc(Long quizId, Long assignmentId, Long userId);
  List<BasicQuizSubmission> findByQuizIdInAndAssignmentIdAndUserIdOrderByIdDesc(List<Long> quizIds, Long assignmentId, Long userId);
}
