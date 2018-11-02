package cn.thoughtworks.school.entities;

import javax.persistence.*;

@Entity
@Table(name = "userQuizGroup")
public class UserQuizGroup {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long userId;
  private Long quizGroupId;
  private String createTime;

  public UserQuizGroup() {
  }

  public UserQuizGroup(Long userId, Long quizGroupId, String createTime) {
    this.userId = userId;
    this.quizGroupId = quizGroupId;
    this.createTime = createTime;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getQuizGroupId() {
    return quizGroupId;
  }

  public void setQuizGroupId(Long quizGroupId) {
    this.quizGroupId = quizGroupId;
  }
}
