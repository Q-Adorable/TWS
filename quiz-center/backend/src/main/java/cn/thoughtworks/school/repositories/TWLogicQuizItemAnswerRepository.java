package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.TWLogicQuizItemAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TWLogicQuizItemAnswerRepository extends JpaRepository<TWLogicQuizItemAnswer, Long> {
}
