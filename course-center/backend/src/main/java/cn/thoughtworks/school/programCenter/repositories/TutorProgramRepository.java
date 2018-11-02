package cn.thoughtworks.school.programCenter.repositories;

import cn.thoughtworks.school.programCenter.entities.TutorProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface TutorProgramRepository extends JpaRepository<TutorProgram, Long> {
    List<TutorProgram> findByTutorId(Long tutorId);

    TutorProgram findByTutorIdAndProgramId(Long tutorId, Long programId);

    @Modifying
    @Transactional
    @Query("delete from TutorProgram where programId=?1")
    void deleteByProgramId(Long programId);

    List<TutorProgram> findByProgramIdAndTutorIdIn(Long programId, List<Long> userIds);

    List<TutorProgram> findByProgramId(Long programId);
}
