package cn.thoughtworks.school.entities;

import javax.persistence.*;

@Entity
@Table(name = "subjectiveSubmission")
public class SubjectiveQuizSubmission {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long assignmentId;
  private Long quizId;
  private Long userId;
  private String userAnswer;
  private String submitTime;

  public SubjectiveQuizSubmission() {
  }

  public SubjectiveQuizSubmission(Long assignmentId, Long quizId, Long userId, String userAnswer) {
    this.assignmentId = assignmentId;
    this.quizId = quizId;
    this.userId = userId;
    this.userAnswer = userAnswer;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getAssignmentId() {
    return assignmentId;
  }

  public void setAssignmentId(Long assignmentId) {
    this.assignmentId = assignmentId;
  }

  public Long getQuizId() {
    return quizId;
  }

  public void setQuizId(Long quizId) {
    this.quizId = quizId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUserAnswer() {
    return userAnswer;
  }

  public void setUserAnswer(String userAnswer) {
    this.userAnswer = userAnswer;
  }

  public String getSubmitTime() {
    return submitTime;
  }

  public void setSubmitTime(String submitTime) {
    this.submitTime = submitTime;
  }
}
