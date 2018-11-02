package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface TagRepository extends JpaRepository<Tag,Long>{
  List<Tag> getTagsByNameContaining(String query,Pageable pageable);
  Stream<Tag> findTagByName(String name);

}
