package cn.thoughtworks.school.programCenter.repositories;

import cn.thoughtworks.school.programCenter.entities.ReviewQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ReviewQuizRepository extends JpaRepository<ReviewQuiz, Long> {
    ReviewQuiz findBySectionIdAndStudentId(Long sectionId, Long studentId);

    @Query(" select sum(t.grade) as grade from ReviewQuiz t where t.studentId = ?1 and t.taskId in ?2")
    Long getScores(Long studentId, List<Long> ids);

    List<ReviewQuiz> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

    @Query(" select count(t.id) from ReviewQuiz t where t.studentId = ?1 and t.status!='已提交' and t.taskId in ?2")
    Long getFinishSectionCount(Long studentId, List<Long> taskIds);

    ReviewQuiz findByAssignmentIdAndQuizIdAndStudentId(Long assignmentId, Long quizId, Long studentId);

    List<ReviewQuiz> findAllByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

    List<ReviewQuiz> findAllByAssignmentIdAndQuizIdAndStatus(Long assignmentId, Long quizId, String status);

    List<ReviewQuiz> findAllByTaskIdInAndStudentId(List taskIds, Long userId);

    List<ReviewQuiz> findAllByAssignmentIdAndQuizId(Long assignmentId, Long quizId);

    List<ReviewQuiz> findAllByAssignmentIdAndStudentIdIn(Long id, List<Long> studentIds);
}
