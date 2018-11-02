package cn.thoughtworks.school.programCenter.repositories;

import cn.thoughtworks.school.programCenter.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByProgramIdOrderByOrderNumberAsc(Long programId);
    List<Topic> findByProgramIdAndVisibleOrderByOrderNumberAsc(Long programId, Boolean visible);

    @Modifying
    @Transactional
    @Query("delete from Topic where programId=?1")
    void deleteByProgramId(Long programId);

    List<Topic> findByProgramId(Long programId);
}
