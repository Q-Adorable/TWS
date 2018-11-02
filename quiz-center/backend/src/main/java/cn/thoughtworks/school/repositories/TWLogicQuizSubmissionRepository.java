package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.TWLogicQuizSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface TWLogicQuizSubmissionRepository extends JpaRepository<TWLogicQuizSubmission, Integer> {
    Stream<TWLogicQuizSubmission> findByAssignmentIdAndUserId(Long assignmentId, Long userId);

    Optional<TWLogicQuizSubmission> findTopByAssignmentIdAndUserId(Long assignmentId, Long userId);

    Optional<TWLogicQuizSubmission> findTopByAssignmentIdAndQuizIdAndUserId(Long assignmentId, Long quizId, Long userId);
}
