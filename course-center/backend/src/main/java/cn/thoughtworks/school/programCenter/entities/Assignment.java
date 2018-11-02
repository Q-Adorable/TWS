package cn.thoughtworks.school.programCenter.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "assignment")
@Entity
public class Assignment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long taskId;
  private String type;
  private String title;
  private String createTime;
  private Long creatorId;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "assignmentId")
  private List<AssignmentQuiz> selectedQuizzes;

  public Assignment(Long taskId, String type, String title, String createTime, Long creatorId, List<AssignmentQuiz> selectedQuizzes) {
    this.taskId = taskId;
    this.type = type;
    this.title = title;
    this.createTime = createTime;
    this.creatorId = creatorId;
    this.selectedQuizzes = selectedQuizzes;
  }

  public Assignment(Long id, String type, String title, Long creatorId, List<AssignmentQuiz> assignmentQuizzes) {
    this.taskId = id;
    this.type = type;
    this.title = title;
    this.creatorId = creatorId;
    this.selectedQuizzes = assignmentQuizzes;
  }

  public Assignment(Long taskId, String type, String createTime, Long creatorId, String title) {
    this.taskId = taskId;
    this.type = type;
    this.title = title;
    this.creatorId = creatorId;
    this.createTime = createTime;
  }

  public List<AssignmentQuiz> getSelectedQuizzes() {
    return selectedQuizzes;
  }

  public void setSelectedQuizzes(List<AssignmentQuiz> selectedQuizzes) {
    this.selectedQuizzes = selectedQuizzes;
  }

  public Long getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(Long creatorId) {
    this.creatorId = creatorId;
  }

  public Assignment() {
  }

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTaskId() {
    return taskId;
  }

  public void setTaskId(Long taskId) {
    this.taskId = taskId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Assignment copyAssignment(Assignment assignment, Task task) {
    List<AssignmentQuiz> assignmentQuizzes = assignment.getSelectedQuizzes().stream()
            .map(assignmentQuiz -> new AssignmentQuiz(assignmentQuiz.getQuizId(),assignment.getCreatorId())).collect(Collectors.toList());
    return new Assignment(task.getId(),assignment.getType(),assignment.getTitle(),
            assignment.getCreatorId(),assignmentQuizzes);
  }
}