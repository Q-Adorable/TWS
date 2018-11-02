package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.OnlineCodingSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;


public interface OnlineCodingSubmissionRepository extends JpaRepository<OnlineCodingSubmission, Long> {
  List<OnlineCodingSubmission> findByQuizIdAndAssignmentIdOrderBySubmitTimeDesc(Long quizId, Long assignmentId);
  ArrayList<OnlineCodingSubmission> findByQuizIdAndAssignmentIdAndUserIdOrderByIdDesc(Long quizId, Long assignmentId, Long userId);
  List<OnlineCodingSubmission> findByQuizIdInAndAssignmentIdAndUserIdOrderByIdDesc(List<Long> quizIds, Long assignmentId, Long userId);
}
