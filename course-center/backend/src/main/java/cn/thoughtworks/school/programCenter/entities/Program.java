package cn.thoughtworks.school.programCenter.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "program")
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;
    @Column
    private String createTime;
    @Column
    private String startTime;
    @Column
    private String endTime;

    @Column
    private Long creatorId;

    @Column
    private String category;
    @Column
    private String introduction;
    @Column
    private boolean studentLink;
    @Column
    private boolean tutorLink;

    @Transient
    private List<Topic> topics;

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public Program() {

    }

    public Program(String title, String createTime, String startTime, String endTime, Long creatorId, String category, String introduction) {
        this.title = title;
        this.createTime = createTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.creatorId = creatorId;
        this.category = category;
        this.introduction = introduction;
    }

    public Program(String title, String startTime, String endTime, Long creatorId, String category, String introduction) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.creatorId = creatorId;
        this.category = category;
        this.introduction = introduction;

    }

    public boolean isTutorLink() {
        return tutorLink;
    }

    public void setTutorLink(boolean tutorLink) {
        this.tutorLink = tutorLink;
    }

    public boolean isStudentLink() {
        return studentLink;
    }

    public void setStudentLink(boolean studentLink) {
        this.studentLink = studentLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getCreateTime() {
        return createTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Program copy(Program program, Long creatorId) {
        return new Program("copy " + program.getTitle(), program.getStartTime(),
                program.getEndTime(), creatorId, program.getCategory(), program.getIntroduction());
    }
}
