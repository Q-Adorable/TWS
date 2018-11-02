package cn.thoughtworks.school.programCenter.repositories;

import cn.thoughtworks.school.programCenter.entities.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    Optional<Program> findProgramByCategoryAndId(String category, Long programId);

    @Query(value = "select * from program where id in (select programId from userProgram where userProgram.userId=?1)", nativeQuery = true)
    List<Program> getProgramsByUserProgramStudentId(long id);

    @Query(value = "select * from program where id in (select programId from tutorProgram where tutorId=?1)", nativeQuery = true)
    List<Program> getProgramsByUserProgramTutorId(long id);

    Page<Program> findByIdIn(List<Long> programIds,Pageable pageable);
}
