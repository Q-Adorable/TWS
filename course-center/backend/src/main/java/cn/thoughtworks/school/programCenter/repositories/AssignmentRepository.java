package cn.thoughtworks.school.programCenter.repositories;

import cn.thoughtworks.school.programCenter.entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByTaskIdOrderByIdDesc(Long taskId);

    @Query("select assignment from Assignment assignment where taskId in ?1")
    List<Assignment> findByTaskIds(List<Long> taskIds);

    List<Assignment> findByTaskIdOrderById(Long taskId);
}
