package cn.thoughtworks.school.programCenter.entities;

import javax.persistence.*;

@Entity
@Table(name = "taskSuggestion")
public class TaskSuggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long taskId;
    @Column
    private Long studentId;
    @Column
    private String content;
    @Column
    private String createTime;

    public TaskSuggestion(Long id, Long taskId, Long studentId, String content, String createTime) {
        this.id = id;
        this.taskId = taskId;
        this.studentId = studentId;
        this.content = content;
        this.createTime = createTime;
    }
    public TaskSuggestion(){

    }

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
