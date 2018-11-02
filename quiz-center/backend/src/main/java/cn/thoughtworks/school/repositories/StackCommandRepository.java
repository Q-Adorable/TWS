package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.StackCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author yywei
 **/
@Repository
public interface StackCommandRepository extends JpaRepository<StackCommand, Long> {

    StackCommand findByName(String name);
}
