package cn.thoughtworks.school.programCenter.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long programId;
    @Column
    private String title;

    @Column
    private String createTime;
    @Column
    private Long orderNumber;
    private Boolean visible;

    public Topic(Long programId, String title, String createTime, Long orderNumber, Boolean visible) {
        this.programId = programId;
        this.title = title;
        this.createTime = createTime;
        this.orderNumber = orderNumber;
        this.visible = visible;
    }

    public Topic() {
    }

    public Topic(Long programId, String title, Long orderNumber, Boolean visible) {
        this.programId = programId;
        this.title = title;
        this.orderNumber = orderNumber;
        this.visible = visible;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public Topic copy(Topic topic,Program program) {
        return new Topic(program.getId(),topic.getTitle(), topic.getOrderNumber(),topic.getVisible());
    }
}
