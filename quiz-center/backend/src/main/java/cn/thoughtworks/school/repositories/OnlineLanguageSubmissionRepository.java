package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.OnlineLanguageSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;


public interface OnlineLanguageSubmissionRepository extends JpaRepository<OnlineLanguageSubmission, Long> {
    List<OnlineLanguageSubmission> findByQuizIdAndAssignmentIdOrderBySubmitTimeDesc(Long quizId, Long assignmentId);

    ArrayList<OnlineLanguageSubmission> findByQuizIdAndAssignmentIdAndUserIdOrderByIdDesc(Long quizId, Long assignmentId, Long userId);

    List<OnlineLanguageSubmission> findByQuizIdInAndAssignmentIdAndUserIdOrderByIdDesc(List<Long> quizIds, Long assignmentId, Long userId);

    Page<OnlineLanguageSubmission> findByUserIdAndAssignmentIdAndQuizId(Long userId, Long assignmentId, Long quizId, Pageable pageable);
}
