package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.LogicQuizItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface LogicQuizItemRepository extends JpaRepository<LogicQuizItem, Integer> {
    // TODO 大于7500 出现乱序
    @Query(value = "SELECT * FROM logicQuizItem  WHERE count BETWEEN 15  AND 25 AND id < 7500 ORDER BY rand() LIMIT ?1", nativeQuery = true)
    Stream<LogicQuizItem> getEasyItems(int easyCount);

    @Query(value = "SELECT * FROM logicQuizItem  WHERE count BETWEEN 26  AND 33 AND id < 7500  ORDER BY rand() LIMIT ?1", nativeQuery = true)
    Stream<LogicQuizItem> getNormalItems(int normalCount);

    @Query(value = "SELECT * FROM logicQuizItem  WHERE count BETWEEN 34  AND 50 AND id < 7500  ORDER BY rand() LIMIT ?1", nativeQuery = true)
    Stream<LogicQuizItem> getHardItems(int hardCount);

    @Query(value = "SELECT * FROM logicQuizItem  WHERE count < 15 LIMIT 1", nativeQuery = true)
    Stream<LogicQuizItem> getExampleItems(int exampleCount);

    Stream<LogicQuizItem> findTop2ByCountLessThan(Integer count);

    LogicQuizItem findByIdAndAnswer(int id, String answer);

    Stream<LogicQuizItem> findByIdIn(List<Integer> ids);
}
