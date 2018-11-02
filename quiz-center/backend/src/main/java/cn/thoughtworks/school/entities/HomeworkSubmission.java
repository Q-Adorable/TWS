package cn.thoughtworks.school.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "homeworkSubmission")
public class HomeworkSubmission {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long assignmentId;
  private Long quizId;
  private Long userId;
  private String userAnswer;
  private Date submitTime;
  private Integer status;
  private String result;
  private String answerBranch;
  private Long buildNumber;
  private String commitId;

  public HomeworkSubmission() {
  }

  public HomeworkSubmission(Long assignmentId, Long quizId, Long userId, String userAnswer, String answerBranch,Date submitTime) {
    this.submitTime = submitTime;
    this.assignmentId = assignmentId;
    this.quizId = quizId;
    this.userId = userId;
    this.userAnswer = userAnswer;
    this.answerBranch = answerBranch;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
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

  public Date getSubmitTime() {
    return submitTime;
  }

  public void setSubmitTime(Date submitTime) {
    this.submitTime = submitTime;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getAnswerBranch() {
    return answerBranch;
  }

  public void setAnswerBranch(String answerBranch) {
    this.answerBranch = answerBranch;
  }

  public Long getBuildNumber() {
    return buildNumber;
  }

  public void setBuildNumber(Long buildNumber) {
    this.buildNumber = buildNumber;
  }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }
}
