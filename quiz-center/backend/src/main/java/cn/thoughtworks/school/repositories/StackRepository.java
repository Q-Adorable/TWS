package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.Stack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StackRepository extends JpaRepository<Stack, Long> {
  Stack findStackByImage(String definition);
}
