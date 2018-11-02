package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.PractiseDiary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PractiseDiaryRepository extends JpaRepository<PractiseDiary, Long> {
    List<PractiseDiary> findByAuthorId(Long authorId, Pageable pageable);

    List<PractiseDiary> findByAuthorIdOrderByDateDesc(Long authorId);

    List<PractiseDiary> findAllByIdIn(List<Long> ids);
}
