package cn.thoughtworks.school.repositories;


import cn.thoughtworks.school.entities.SubjectiveQuizSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface SubjectiveQuizSubmissionRepository extends JpaRepository<SubjectiveQuizSubmission,Long>{

  List<SubjectiveQuizSubmission> findByQuizIdAndAssignmentIdOrderBySubmitTimeDesc(Long quizId, Long assignmentId);
  ArrayList<SubjectiveQuizSubmission> findByQuizIdAndAssignmentIdAndUserIdOrderByIdDesc(Long quizId, Long assignmentId, Long userId);
  List<SubjectiveQuizSubmission> findByQuizIdInAndAssignmentIdAndUserIdOrderByIdDesc(List<Long> quizIds, Long assignmentId, Long userId);

    List<SubjectiveQuizSubmission> findAllByAssignmentIdAndQuizId(Long oldAssignmentId, Long quizId);
}
