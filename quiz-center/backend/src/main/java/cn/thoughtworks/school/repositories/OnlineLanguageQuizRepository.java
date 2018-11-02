package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.OnlineLanguageQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OnlineLanguageQuizRepository extends JpaRepository<OnlineLanguageQuiz, Long> {
    List<OnlineLanguageQuiz> findByIdInAndIsAvailableIsTrue(List<Long> ids);
    OnlineLanguageQuiz findByIdAndIsAvailableIsTrue(Long id);
    List<OnlineLanguageQuiz> findByOnlineLanguageNameAndLanguageAndStatusOrderByCreateTimeDesc(String onlineLanguageName, String language, int status);

    Page<OnlineLanguageQuiz> findAllByIsAvailableIsTrue(Pageable pageable);
    Page<OnlineLanguageQuiz> findAllByIsAvailableIsTrueAndStatus(int status, Pageable pageable);

    Page<OnlineLanguageQuiz> findAllByIsAvailableIsTrueAndMakerIdAndStatus(Long makerId, int status, Pageable pageable);
    Page<OnlineLanguageQuiz> findAllByIsAvailableIsTrueAndMakerId(Long makerId, Pageable pageable);

    @Query(value = "SELECT distinct b FROM OnlineLanguageQuiz b JOIN b.tags t WHERE b.isAvailable=true AND t.name in :names")
    Page<OnlineLanguageQuiz> findAllByIsAvailableAndTag(@Param("names") String[] names, Pageable pageable);

    @Query(value = "SELECT distinct b FROM OnlineLanguageQuiz b JOIN b.tags t WHERE (b.isAvailable=true AND t.name in :names) AND b.status=:status")
    Page<OnlineLanguageQuiz> findAllByIsAvailableAndTagAndStatus(@Param("names") String[] names, @Param("status") int status, Pageable pageable);

    @Query(value = "SELECT distinct b FROM OnlineLanguageQuiz b JOIN b.tags t WHERE b.isAvailable=true AND t.name in :names AND t.makerId=:makerId")
    Page<OnlineLanguageQuiz> findAllByIsAvailableAndTagsAndMakerId(@Param("names") String[] names, @Param("makerId") Long makerId, Pageable pageable);

    @Query(value = "SELECT distinct b FROM OnlineLanguageQuiz b JOIN b.tags t WHERE b.isAvailable=true AND t.name in :names AND t.makerId=:makerId and b.status=:status")
    Page<OnlineLanguageQuiz> findAllByIsAvailableAndTagsAndMakerIdAndStatus(@Param("names") String[] names, @Param("makerId") Long makerId, @Param("status") int status, Pageable pageable);

    Page<OnlineLanguageQuiz> findAllByStatus(int status, Pageable pageable);

    Page<OnlineLanguageQuiz> findAllByIdAndStatus(Long id, int status, Pageable pageable);

    Page<OnlineLanguageQuiz> findByRemarkLikeAndIsAvailable(String remark,Boolean available,Pageable pageable);

    Page<OnlineLanguageQuiz> findByDescriptionLikeAndIsAvailable(String description,Boolean available,Pageable pageable);

}
