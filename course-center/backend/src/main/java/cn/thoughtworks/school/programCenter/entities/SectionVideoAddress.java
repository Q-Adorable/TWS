package cn.thoughtworks.school.programCenter.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sectionVideoAddress")
public class SectionVideoAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long taskId;
    @Column
    private Long sectionId;
    @Column
    private Date createTime;
    @Column
    private String videoAddress;

    public SectionVideoAddress(Long taskId, Long sectionId, Date createTime, String videoAddress) {
        this.taskId = taskId;
        this.sectionId = sectionId;
        this.createTime = createTime;
        this.videoAddress = videoAddress;
    }

    public SectionVideoAddress() {

    }

    public String getVideoAddress() {
        return videoAddress;
    }

    public void setVideoAddress(String videoAddress) {
        this.videoAddress = videoAddress;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }
}
