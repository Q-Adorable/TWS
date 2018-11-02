package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.HomeworkSubmission;
import cn.thoughtworks.school.entities.SubjectiveQuizSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface HomeworkSubmissionRepository extends JpaRepository<HomeworkSubmission, Long> {


    List<HomeworkSubmission> findAllByAssignmentIdAndQuizId(Long oldAssignmentId, Long quizId);

    List<HomeworkSubmission> findByQuizIdAndAssignmentIdOrderBySubmitTimeDesc(Long quizId, Long assignmentId);

    ArrayList<HomeworkSubmission> findByQuizIdAndAssignmentIdAndUserIdOrderByIdDesc(Long quizId, Long assignmentId, Long userId);

    List<HomeworkSubmission> findByQuizIdInAndAssignmentIdAndUserIdOrderByIdDesc(List<Long> quizIds, Long assignmentId, Long userId);

    Page<HomeworkSubmission> findByQuizIdAndAssignmentIdAndUserId(Long quizId, Long assignmentId, Long userId, Pageable pageable);

}
