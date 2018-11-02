package cn.thoughtworks.school.programCenter.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "quizSuggestion")
@Setter
@Getter
public class QuizSuggestion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long sectionId;
  private Long toUserId;
  private Long fromUserId;
  private String content;
  private String createTime;
  private Long assignmentId;
  private Long quizId;
  private Long parentId;
  @Transient
  private User fromUser;
  @Transient
  private User toUser;
  @Transient
  private List<QuizSuggestion> childSuggestions;
}
