package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.HomeworkQuiz;
import cn.thoughtworks.school.enums.JobState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HomeworkQuizRepository extends JpaRepository<HomeworkQuiz, Long> {
    List<HomeworkQuiz> findByIdInAndIsAvailableIsTrue(List<Long> ids);

    HomeworkQuiz findByIdAndIsAvailableIsTrue(Long id);

    HomeworkQuiz findByIdAndIsAvailableIsTrueAndQuizGroupIdIn(Long id, List quizGroupIds);

    Page<HomeworkQuiz> findAllByIsAvailableIsTrue(Pageable pageable);

    Page<HomeworkQuiz> findAllByIsAvailableIsTrueAndQuizGroupIdIn(List groupIds, Pageable pageable);

    Page<HomeworkQuiz> findAllByIsAvailableIsTrueAndStatus(int status, Pageable pageable);

    Page<HomeworkQuiz> findAllByIsAvailableIsTrueAndStatusAndQuizGroupIdIn(int status, List quizGroupIds, Pageable pageable);

    Page<HomeworkQuiz> findAllByIsAvailableIsTrueAndMakerIdAndStatus(Long makerId, int status, Pageable pageable);

    Page<HomeworkQuiz> findAllByIsAvailableIsTrueAndMakerIdAndStatusAndQuizGroupIdIn(Long makerId, int status, List quizGroupIds, Pageable pageable);

    Page<HomeworkQuiz> findAllByIsAvailableIsTrueAndMakerId(Long makerId, Pageable pageable);

    Page<HomeworkQuiz> findAllByIsAvailableIsTrueAndMakerIdAndQuizGroupIdIn(Long makerId, List quizGroupIds, Pageable pageable);

    @Query(value = "SELECT distinct b FROM HomeworkQuiz b JOIN b.tags t WHERE b.isAvailable=true AND t.name in :names")
    Page<HomeworkQuiz> findAllByIsAvailableAndTag(@Param("names") String[] names, Pageable pageable);

    @Query(value = "SELECT distinct b FROM HomeworkQuiz b JOIN b.tags t WHERE b.isAvailable=true AND b.quizGroupId in:quizGroupIds  AND t.name in :names")
    Page<HomeworkQuiz> findAllByIsAvailableAndTagAndQuizGroupIds(@Param("names") String[] names, @Param("quizGroupIds") List quizGroupIds, Pageable pageable);

    @Query(value = "SELECT distinct b FROM HomeworkQuiz b JOIN b.tags t WHERE (b.isAvailable=true AND t.name in :names) AND b.status=:status")
    Page<HomeworkQuiz> findAllByIsAvailableAndTagAndStatus(@Param("names") String[] names, @Param("status") int status, Pageable pageable);

    @Query(value = "SELECT distinct b FROM HomeworkQuiz b JOIN b.tags t WHERE (b.isAvailable=true AND b.quizGroupId in:quizGroupIds AND t.name in :names) AND b.status=:status")
    Page<HomeworkQuiz> findAllByIsAvailableAndTagAndStatusAndQuizGroupIdIn(@Param("names") String[] names, @Param("status") int status, @Param("quizGroupIds") List quizGroupIds, Pageable pageable);

    @Query(value = "SELECT distinct b FROM HomeworkQuiz b JOIN b.tags t WHERE b.isAvailable=true AND t.name in :names AND t.makerId=:makerId")
    Page<HomeworkQuiz> findAllByIsAvailableAndTagsAndMakerId(@Param("names") String[] names, @Param("makerId") Long makerId, Pageable pageable);

    @Query(value = "SELECT distinct b FROM HomeworkQuiz b JOIN b.tags t WHERE b.isAvailable=true AND t.name in :names And b.quizGroupId in:quizGroupIds  AND t.makerId=:makerId")
    Page<HomeworkQuiz> findAllByIsAvailableAndTagsAndMakerIdAndQuizGroupIdIn(@Param("names") String[] names, @Param("makerId") Long makerId, @Param("quizGroupIds") List quizGroupIds, Pageable pageable);

    @Query(value = "SELECT distinct b FROM HomeworkQuiz b JOIN b.tags t WHERE b.isAvailable=true AND t.name in :names AND t.makerId=:makerId and b.status=:status")
    Page<HomeworkQuiz> findAllByIsAvailableAndTagsAndMakerIdAndStatus(@Param("names") String[] names, @Param("makerId") Long makerId, @Param("status") int status, Pageable pageable);

    @Query(value = "SELECT distinct b FROM HomeworkQuiz b JOIN b.tags t WHERE b.isAvailable=true AND t.name in :names AND t.makerId=:makerId and b.quizGroupId in:quizGroupIds and b.status=:status")
    Page<HomeworkQuiz> findAllByIsAvailableAndTagsAndMakerIdAndStatusAndQuizGroupIdIn(@Param("names") String[] names, @Param("makerId") Long makerId, @Param("status") int status, @Param("quizGroupIds") List quizGroupIds, Pageable pageable);


    Page<HomeworkQuiz> findAllByStatus(int status, Pageable pageable);

    Page<HomeworkQuiz> findAllByIdAndStatus(Long id, int status, Pageable pageable);

    Page<HomeworkQuiz> findByRemarkLikeAndIsAvailable(String remark, Boolean available,Pageable pageable);

    Page<HomeworkQuiz> findByDescriptionLikeAndIsAvailable(String description,Boolean available, Pageable pageable);
}
