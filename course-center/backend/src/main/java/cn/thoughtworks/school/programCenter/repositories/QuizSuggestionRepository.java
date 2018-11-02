package cn.thoughtworks.school.programCenter.repositories;

import cn.thoughtworks.school.programCenter.entities.QuizSuggestion;
import cn.thoughtworks.school.programCenter.entities.ReviewQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizSuggestionRepository extends JpaRepository<QuizSuggestion, Long> {
    @Query(value = "select * from quizSuggestion where assignmentId=?1 and quizId=?2 and (fromUserId=?3 or toUserId=?3) order by createTime asc ", nativeQuery = true)
    List<QuizSuggestion> findByAssignmentIdAndQuizIdAndStudentId(Long assignmentId, Long quizId, Long studentId);

    List<QuizSuggestion> findAllByAssignmentIdAndQuizId(Long assignmentId, Long quizId);
}
