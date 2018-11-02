package cn.thoughtworks.school.repositories;


import cn.thoughtworks.school.entities.Comment;
import cn.thoughtworks.school.entities.PractiseDiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPractiseDiaryId(Long id);
}
