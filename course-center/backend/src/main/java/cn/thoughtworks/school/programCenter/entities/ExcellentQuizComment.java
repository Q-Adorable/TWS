package cn.thoughtworks.school.programCenter.entities;

import javax.persistence.*;

@Entity
@Table(name = "excellentQuizComment")
public class ExcellentQuizComment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long assignmentId;
  private Long quizId;
  private Long sectionId;
  private Long studentId;
  private Long commenterId;
  private String content;
  private String createTime;

  public ExcellentQuizComment(Long sectionId, Long studentId, Long commenterId, String content, String createTime) {
    this.sectionId = sectionId;
    this.studentId = studentId;
    this.commenterId = commenterId;
    this.content = content;
    this.createTime = createTime;
  }

  public ExcellentQuizComment() {

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

  public Long getCommenterId() {
    return commenterId;
  }

  public void setCommenterId(Long commenterId) {
    this.commenterId = commenterId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSectionId() {
    return sectionId;
  }

  public void setSectionId(Long sectionId) {
    this.sectionId = sectionId;
  }

  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }
}
