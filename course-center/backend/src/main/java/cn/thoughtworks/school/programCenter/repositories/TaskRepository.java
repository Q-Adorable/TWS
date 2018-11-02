package cn.thoughtworks.school.programCenter.repositories;

import cn.thoughtworks.school.programCenter.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTaskByTopicIdOrderByOrderNumberAsc(Long topicId);

    List<Task> findTaskByProgramIdAndVisible(Long programId, Boolean visible);


    List<Task> findByProgramIdAndVisibleOrderByOrderNumberAsc(Long programId, Boolean visible);


    List<Task> findByProgramId(Long programId);

    @Modifying
    @Transactional
    @Query("delete from Task where programId=?1")
    void deleteByProgramId(Long programId);

    @Modifying
    @Transactional
    @Query("update Task set visible=?2 where topicId=?1")
    void updateVisibilityByTopicId(Long id, Boolean visible);
    
    List<Task> findByProgramIdAndTopicIdAndOrderNumberGreaterThanOrderByOrderNumberAsc(Long programId, Long topicId, Long orderNumber);

    List<Task> findByProgramIdAndTopicIdAndVisibleIsTrueOrderByOrderNumberAsc(Long programId, Long topicId);

    List<Task> findByProgramIdAndTopicIdAndOrderNumberLessThanOrderByOrderNumberDesc(Long programId, Long topicId, Long orderNumber);

//    @Query("select t from Task t where t.visible=1 and t.topicId in ?1")
//    List<Task> findByTopicIds(List<Long> topicIds);
    List<Task> findByTopicIdIn(List<Long> topicIds);

    @Modifying
    @Transactional
    @Query("update Task set programId=?2, topicId=?3 where id in ?1")
    void moveTasksToTopic(List<Long> sourceTaskIds, Long programId, Long topicId);

    List<Task> findByTopicIdAndVisibleOrderByOrderNumberAsc(Long programId, Boolean visible);
}
