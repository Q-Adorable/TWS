package cn.thoughtworks.school.programCenter.repositories;

import cn.thoughtworks.school.programCenter.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Stream<Tag> findByName(String name);
}
