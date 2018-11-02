package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.ExcellentDiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExcellentDiaryRepository extends JpaRepository<ExcellentDiary, Long> {
    Page<ExcellentDiary> findAll(Pageable pageable);
    ExcellentDiary findByDiaryId(Long excellentDiaryId);
}
