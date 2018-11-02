package cn.thoughtworks.school.programCenter.entities;

import javax.persistence.*;

@Entity
@Table(name = "taskFeeling")
public class TaskFelling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private Long taskId;
    @Column
    private Long studentId;
    @Column
    private String feeling;
    @Column
    private String createTime;

    public TaskFelling(Long id, Long taskId, Long studentId, String feeling, String createTime) {
        this.id = id;
        this.taskId = taskId;
        this.studentId = studentId;
        this.feeling = feeling;
        this.createTime = createTime;
    }

    public TaskFelling(){

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

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
