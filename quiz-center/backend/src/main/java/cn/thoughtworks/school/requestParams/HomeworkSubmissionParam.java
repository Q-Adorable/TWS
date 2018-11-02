package cn.thoughtworks.school.requestParams;

import javax.validation.constraints.NotNull;

public class HomeworkSubmissionParam {

    @NotNull
    private String userAnswerRepo;

    private String branch = "master";

    @NotNull
    private Long quizId;

    @NotNull
    private Long assignmentId;

    @NotNull
    private Long studentId;

    public String getUserAnswerRepo() {
        return userAnswerRepo;
    }

    public void setUserAnswerRepo(String userAnswerRepo) {
        this.userAnswerRepo = userAnswerRepo;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
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

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "HomeworkSubmissionParam{" +
                "userAnswerRepo='" + userAnswerRepo + '\'' +
                ", branch='" + branch + '\'' +
                ", quizId='" + quizId + '\'' +
                ", assignmentId=" + assignmentId +
                ", studentId=" + studentId +
                '}';
    }
}

