package cn.thoughtworks.school.programCenter.entities;

import javax.persistence.*;

@Table(name = "assignmentQuiz")
@Entity
public class AssignmentQuiz {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long quizId;
  private Long assignmentId;
  private String createTime;
  private Long creatorId;

  public AssignmentQuiz() {
  }

  public AssignmentQuiz(Long quizId, String createTime, Long creatorId) {
    this.quizId = quizId;
    this.createTime = createTime;
    this.creatorId = creatorId;
  }

  public AssignmentQuiz(Long quizId, Long creatorId) {
    this.quizId = quizId;
    this.creatorId = creatorId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getQuizId() {
    return quizId;
  }

  public void setQuizId(Long quizId) {
    this.quizId = quizId;
  }

  public Long getAssignmentId() {
    return assignmentId;
  }

  public void setAssignmentId(Long assignmentId) {
    this.assignmentId = assignmentId;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public Long getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(Long creatorId) {
    this.creatorId = creatorId;
  }
}
