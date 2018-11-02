package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.OnlineCodingQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OnlineCodingQuizRepository extends JpaRepository<OnlineCodingQuiz, Long> {
    List<OnlineCodingQuiz> findByIdInAndIsAvailableIsTrue(List<Long> ids);
    OnlineCodingQuiz findByIdAndIsAvailableIsTrue(Long id);
    OnlineCodingQuiz findByIdAndIsAvailableIsTrueAndQuizGroupIdIn(Long id, List quizGroupIds);

    List<OnlineCodingQuiz> findByOnlineCodingNameAndLanguageAndStatusOrderByCreateTimeDesc(String onlineCodingName, String language, int status);

    Page<OnlineCodingQuiz> findAllByIsAvailableIsTrue(Pageable pageable);
    Page<OnlineCodingQuiz> findAllByIsAvailableIsTrueAndStatus(int status, Pageable pageable);

    Page<OnlineCodingQuiz> findAllByIsAvailableIsTrueAndMakerIdAndStatus(Long makerId, int status, Pageable pageable);
    Page<OnlineCodingQuiz> findAllByIsAvailableIsTrueAndMakerId(Long makerId, Pageable pageable);

    @Query(value = "SELECT distinct b FROM OnlineCodingQuiz b JOIN b.tags t WHERE b.isAvailable=true AND t.name in :names")
    Page<OnlineCodingQuiz> findAllByIsAvailableAndTag(@Param("names") String[] names, Pageable pageable);

    @Query(value = "SELECT distinct b FROM OnlineCodingQuiz b JOIN b.tags t WHERE (b.isAvailable=true AND t.name in :names) AND b.status=:status")
    Page<OnlineCodingQuiz> findAllByIsAvailableAndTagAndStatus(@Param("names") String[] names, @Param("status") int status, Pageable pageable);

    @Query(value = "SELECT distinct b FROM OnlineCodingQuiz b JOIN b.tags t WHERE b.isAvailable=true AND t.name in :names AND t.makerId=:makerId")
    Page<OnlineCodingQuiz> findAllByIsAvailableAndTagsAndMakerId(@Param("names") String[] names, @Param("makerId") Long makerId, Pageable pageable);

    @Query(value = "SELECT distinct b FROM OnlineCodingQuiz b JOIN b.tags t WHERE b.isAvailable=true AND t.name in :names AND t.makerId=:makerId and b.status=:status")
    Page<OnlineCodingQuiz> findAllByIsAvailableAndTagsAndMakerIdAndStatus(@Param("names") String[] names, @Param("makerId") Long makerId, @Param("status") int status, Pageable pageable);

    Page<OnlineCodingQuiz> findAllByStatus(int status, Pageable pageable);

    Page<OnlineCodingQuiz> findAllByIdAndStatus(Long id, int status, Pageable pageable);
}
