package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.SubjectiveQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectiveQuizRepository extends JpaRepository<SubjectiveQuiz, Long> {
    Page<SubjectiveQuiz> findAllByIsAvailable(Boolean isAvailable, Pageable pageable);

    Page<SubjectiveQuiz> findAllByIsAvailableAndQuizGroupIdIn(Boolean isAvailable, List quizGroupIds, Pageable pageable);

    @Query(value = "SELECT distinct b FROM SubjectiveQuiz b JOIN b.tags t WHERE  b.isAvailable=true AND t.name in :names")
    Page<SubjectiveQuiz> findAllByIsAvailableAndTags(@Param("names") String[] names, Pageable pageable);

    @Query(value = "SELECT distinct b FROM SubjectiveQuiz b JOIN b.tags t WHERE  b.isAvailable=true AND b.quizGroupId in:quizGroupIds AND t.name in :names")
    Page<SubjectiveQuiz> findAllByIsAvailableAndTagsAndQuizGroupIds(@Param("names") String[] names, @Param("quizGroupIds") List quizGroupIds, Pageable pageable);

    Page<SubjectiveQuiz> findAllByIsAvailableAndMakerId(Boolean isAvailable, Long makerId, Pageable pageable);

    Page<SubjectiveQuiz> findAllByIsAvailableAndMakerIdAndQuizGroupIdIn(Boolean isAvailable, Long makerId, List quizGroupIds, Pageable pageable);


    Page<SubjectiveQuiz> findAllByIsAvailableAndTagsAndMakerId(Boolean isAvailable, String tags, Long makerId, Pageable pageable);

    Page<SubjectiveQuiz> findAllByIsAvailableAndTagsAndMakerIdAndQuizGroupIdIn(Boolean isAvailable, String tags, Long makerId, List quizGroupIds, Pageable pageable);

    Optional<SubjectiveQuiz> findByIdAndIsAvailableIsTrue(Long id);

    SubjectiveQuiz findByIdAndIsAvailableIsTrueAndQuizGroupIdIn(Long id, List quizGroupIds);

    List<SubjectiveQuiz> findAllByIsAvailableIsTrueAndIdIn(List<Long> ids);

    Page findAllById(Long id, Pageable pageable);

    Page<SubjectiveQuiz> findByRemarkLikeAndIsAvailable(String remark, Boolean isAvailable,Pageable pageable);

    Page<SubjectiveQuiz> findByDescriptionLikeAndIsAvailable(String description, Boolean isAvailable,Pageable pageable);
}
