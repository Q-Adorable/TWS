package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.BasicQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BasicQuizRepository extends JpaRepository<BasicQuiz, Long> {
    Page<BasicQuiz> findAllByTypeAndIsAvailable(String Type, Boolean isAvailable, Pageable Pageable);

    Page<BasicQuiz> findAllByTypeAndIsAvailableAndQuizGroupIdIn(String Type, Boolean isAvailable, List quizGroupIds, Pageable Pageable);

    Page<BasicQuiz> findAllByIsAvailable(Boolean isAvailable, Pageable Pageable);

    Page<BasicQuiz> findAllByIsAvailableAndQuizGroupIdIn(Boolean isAvailable, List ids, Pageable Pageable);

    Optional<BasicQuiz> findByIdAndIsAvailableIsTrue(Long id);

    BasicQuiz findByIdAndIsAvailableIsTrueAndQuizGroupIdIn(Long id, List quizGroupIds);

    BasicQuiz findByIdAndIsAvailableIs(Long id, Boolean isAvailable);

    List<BasicQuiz> findAllByIsAvailableIsTrueAndIdIn(List<Long> ids);

    Page<BasicQuiz> findAllByTypeAndIsAvailableAndMakerId(String Type, Boolean isAvailable, Long makerId, Pageable Pageable);

    Page<BasicQuiz> findAllByTypeAndIsAvailableAndMakerIdAndQuizGroupIdIn(String Type, Boolean isAvailable, Long makerId, List quizGroupIds, Pageable Pageable);

    Page<BasicQuiz> findAllByIsAvailableAndMakerId(Boolean isAvailable, Long makerId, Pageable Pageable);

    Page<BasicQuiz> findAllByIsAvailableAndMakerIdAndQuizGroupIdIn(Boolean isAvailable, Long makerId, List quizGroupIds, Pageable Pageable);

    @Query(value = "SELECT distinct b FROM BasicQuiz b JOIN b.tags t WHERE b.type=:type AND b.isAvailable=true AND t.name in :names")
    Page<BasicQuiz> findAllByTypeAndIsAvailableAndTag(@Param("type") String type, @Param("names") String[] names, Pageable pageable);

    @Query(value = "SELECT distinct b FROM BasicQuiz b JOIN b.tags t WHERE b.type=:type AND b.isAvailable=true AND b.quizGroupId in:quizGroupIds AND t.name in :names")
    Page<BasicQuiz> findAllByTypeAndIsAvailableAndTagAndQuizGroupIdIn(@Param("type") String type, @Param("names") String[] names, @Param("quizGroupIds") List quizGroupIds, Pageable pageable);

    @Query(value = "SELECT distinct b FROM BasicQuiz b JOIN b.tags t WHERE b.isAvailable=true AND t.name in :names")
    Page<BasicQuiz> findAllByIsAvailableAndTag(@Param("names") String[] names, Pageable pageable);

    @Query(value = "SELECT distinct b FROM BasicQuiz b JOIN b.tags t WHERE b.isAvailable=true AND b.quizGroupId in :quizGroupIds  AND t.name in :names")
    Page<BasicQuiz> findAllByIsAvailableAndTagAndQuizGroupIdIn(@Param("names") String[] names, @Param("quizGroupIds") List quizGroupIds, Pageable pageable);

    @Query(value = "SELECT distinct b FROM BasicQuiz b JOIN b.tags t WHERE b.type=:type AND b.isAvailable=true AND t.name in :names AND t.makerId=:makerId")
    Page<BasicQuiz> findAllByTypeAndIsAvailableAndTagsAndMakerId(@Param("type") String type, @Param("names") String[] names, @Param("makerId") Long makerId, Pageable pageable);

    @Query(value = "SELECT distinct b FROM BasicQuiz b JOIN b.tags t WHERE b.type=:type AND b.isAvailable=true AND t.name in :names AND b.quizGroupId in:quizGroupIds AND t.makerId=:makerId")
    Page<BasicQuiz> findAllByTypeAndIsAvailableAndTagsAndMakerIdAndQuizGroupIdIn(@Param("type") String type, @Param("names") String[] names, @Param("makerId") Long makerId, @Param("quizGroupIds") List quizGroupIds, Pageable pageable);


    @Query(value = "SELECT distinct b FROM BasicQuiz b JOIN b.tags t WHERE b.isAvailable=true AND t.name in :names AND t.makerId=:makerId")
    Page<BasicQuiz> findAllByIsAvailableAndTagsAndMakerId(@Param("names") String[] names, @Param("makerId") Long makerId, Pageable pageable);

    @Query(value = "SELECT distinct b FROM BasicQuiz b JOIN b.tags t WHERE b.isAvailable=true AND t.name in :names AND b.quizGroupId in:quizGroupIds AND t.makerId=:makerId")
    Page<BasicQuiz> findAllByIsAvailableAndTagsAndMakerIdAndQuizGroupIds(@Param("names") String[] names, @Param("makerId") Long makerId, @Param("quizGroupIds") List quizGroupIds, Pageable pageable);

    Page<BasicQuiz> findAllById(Long id, Pageable pageable);

    Page<BasicQuiz> findByDescriptionLikeAndIsAvailable(String description, Boolean available, Pageable pageable);
}
