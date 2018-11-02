package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.BasicQuizChoices;
import cn.thoughtworks.school.entities.BasicQuizChoicesComplexPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicQuizChoicesRepository extends JpaRepository<BasicQuizChoices, Long> {
    BasicQuizChoices findByComplexPK(BasicQuizChoicesComplexPK complexPK);
}
