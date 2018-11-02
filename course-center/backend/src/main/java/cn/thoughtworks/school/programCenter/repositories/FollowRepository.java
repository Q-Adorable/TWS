package cn.thoughtworks.school.programCenter.repositories;

import cn.thoughtworks.school.programCenter.entities.Follow;
import cn.thoughtworks.school.programCenter.entities.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow,Long>{
    List<Follow> findByProgramIdAndTutorId(Long programId, Long tutorId);
    Follow findByProgramIdAndTutorIdAndStudentId(Long programId, Long tutorId, Long studentId);

    @Modifying
    @Transactional
    @Query(value = "DELETE  FROM  Follow follow WHERE  follow.programId=:programId and follow.tutorId=:tutorId " +
            "and follow.studentId in :studentIds")
    void unFollowByProgramIdAndTutorIdAndStudentIds(@Param("programId") Long programId,
                                                    @Param("tutorId") Long tutorId,
                                                    @Param("studentIds") List<Long> studentIds);

    List<Follow> findByProgramIdAndStudentId(Long programId, Long StudentId);
}
