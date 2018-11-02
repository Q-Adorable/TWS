package cn.thoughtworks.school.programCenter.repositories;

import cn.thoughtworks.school.programCenter.entities.UserProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserProgramRepository extends JpaRepository<UserProgram, Long> {
    List<UserProgram> findByUserId(Long userId);

    List<UserProgram> findByProgramId(Long programId);

    UserProgram findByProgramIdAndUserId(Long programId, Long userId);

    List<UserProgram> findByProgramIdAndUserIdIn(Long programId, List<Long> userIds);

    @Modifying
    @Transactional
    @Query("delete from UserProgram where programId=?1")
    void deleteByProgramId(Long programId);
}
