package cn.thoughtworks.school.programCenter.repositories;

import cn.thoughtworks.school.programCenter.entities.MyWorkspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyWorkspaceRepository extends JpaRepository<MyWorkspace, Long> {
  List<MyWorkspace> findByUserId(Long userId);

  Optional<MyWorkspace> findByProgramIdAndUserId(Long programId, Long id);
}
