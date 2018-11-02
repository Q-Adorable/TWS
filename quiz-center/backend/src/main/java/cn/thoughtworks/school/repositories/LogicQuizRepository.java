package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.LogicQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LogicQuizRepository extends JpaRepository<LogicQuiz, Long> {
    Page<LogicQuiz> findAllByIsAvailable(Boolean isAvailable, Pageable pageable);

    Page<LogicQuiz> findAllByIsAvailableAndQuizGroupIdIn(Boolean isAvailable, List quizGroupIds, Pageable pageable);

    LogicQuiz findByIdAndIsAvailableIsTrue(Long id);

    LogicQuiz findByIdAndIsAvailableIsTrueAndQuizGroupIdIn(Long id, List quizGroupIds);

    Page<LogicQuiz> findByIsAvailableIsTrueAndMakerId(Long makerId, Pageable pageable);

    Page<LogicQuiz> findByIsAvailableIsTrueAndMakerIdAndQuizGroupIdIn(Long makerId, List quizGroupIds, Pageable pageable);

    List<LogicQuiz> findAllByIsAvailableIsTrueAndIdIn(List ids);

    Optional<LogicQuiz> findById(Long integer);

    Page<LogicQuiz> findAllByIsAvailableAndMakerId(Boolean isAvailable, Long makerId, Pageable pageable);

    Page<LogicQuiz> findAllByIsAvailableAndId(boolean b, Long id, Pageable pageable);

    Page<LogicQuiz> findByDescriptionLikeAndIsAvailable(String description, Boolean available, Pageable pageable);
}
