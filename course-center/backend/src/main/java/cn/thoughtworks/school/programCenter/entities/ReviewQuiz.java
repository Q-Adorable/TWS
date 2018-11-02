package cn.thoughtworks.school.programCenter.entities;

import javax.persistence.*;

@Entity
@Table(name = "reviewQuiz")
public class ReviewQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long studentId;
    private Long tutorId;
    private Long sectionId;
    private Long taskId;
    private Long quizId;
    private Long assignmentId;
    private Integer grade;
    private String status;
    private Boolean excellence;
    private String createTime;
    private String supplement;
    public ReviewQuiz() {

    }

    public ReviewQuiz(Long studentId, Long tutorId, Integer grade, Long taskId, Long sectionId, String status, Boolean excellence, String createTime) {
        this.studentId = studentId;
        this.tutorId = tutorId;
        this.grade = grade;
        this.taskId = taskId;
        this.sectionId = sectionId;
        this.status = status;
        this.excellence = excellence;
        this.createTime = createTime;
    }

    public ReviewQuiz(Long studentId, Long tutorId, Long assignmentId, Long taskId, Integer grade, String status) {
        this.studentId = studentId;
        this.tutorId = tutorId;
        this.assignmentId = assignmentId;
        this.grade = grade;
        this.taskId = taskId;
        this.status = status;
    }

    public ReviewQuiz(Long studentId, Long quizId, Long assignmentId, String status) {
        this.studentId = studentId;
        this.quizId = quizId;
        this.assignmentId = assignmentId;
        this.status = status;
    }

    public ReviewQuiz(Long studentId, Long taskId, Long quizId, Long assignmentId, String status, String createTime) {
        this.taskId = taskId;
        this.studentId = studentId;
        this.quizId = quizId;
        this.assignmentId = assignmentId;
        this.status = status;
        this.createTime = createTime;
    }

    public ReviewQuiz(Long studentId, Long taskId, Long quizId, Long assignmentId, String status) {
        this.taskId = taskId;
        this.studentId = studentId;
        this.quizId = quizId;
        this.assignmentId = assignmentId;
        this.status = status;
    }

    public ReviewQuiz(Long studentId, Long assignmentId, Long taskId, int grade, String status, String createTime) {
        this.studentId = studentId;
        this.assignmentId = assignmentId;
        this.taskId = taskId;
        this.grade = grade;
        this.status = status;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getTutorId() {
        return tutorId;
    }

    public void setTutorId(Long tutorId) {
        this.tutorId = tutorId;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getExcellence() {
        return excellence;
    }

    public void setExcellence(Boolean excellence) {
        this.excellence = excellence;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSupplement() {
        return supplement;
    }

    public void setSupplement(String supplement) {
        this.supplement = supplement;
    }
}
