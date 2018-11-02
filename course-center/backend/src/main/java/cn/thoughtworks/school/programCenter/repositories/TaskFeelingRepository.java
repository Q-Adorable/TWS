package cn.thoughtworks.school.programCenter.repositories;

import cn.thoughtworks.school.programCenter.entities.TaskFelling;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskFeelingRepository extends JpaRepository<TaskFelling, Long> {
    TaskFelling findByTaskIdAndStudentId(Long taskId, Long studentId);
}
