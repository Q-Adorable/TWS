package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.QuizGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizGroupRepository extends JpaRepository<QuizGroup, Long> {

    Page<QuizGroup> findByIdIn(List ids, Pageable pageable);

    List<QuizGroup> findByIdIn(List ids);
}
