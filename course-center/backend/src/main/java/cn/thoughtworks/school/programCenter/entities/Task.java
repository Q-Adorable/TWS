package cn.thoughtworks.school.programCenter.entities;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long programId;
    @Column
    private Long paperId;
    @Column
    private Long topicId;
    @Column
    private Long orderNumber;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private String deadLine;
    @Column
    private String createTime;
    private Boolean visible;
    @Column
    private String Link;
    @Column
    private String type;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "taskId")
    private List<Assignment> assignments;

    public Task(Long id, Long paperId, Long topicId, Long orderNumber, String title, String content, String deadLine, Boolean visible, String link, String type) {
        this.programId = id;
        this.paperId = paperId;
        this.topicId = topicId;
        this.orderNumber = orderNumber;
        this.title = title;
        this.content = content;
        this.deadLine = deadLine;
        this.visible = visible;
        this.Link = link;
        this.type = type;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public Task() {
    }

    public Task(Long programId, Long paperId, Long topicId, Long orderNumber, String title, String content, String deadLine, String createTime, Boolean visible, String link, String type) {
        this.programId = programId;
        this.paperId = paperId;
        this.topicId = topicId;
        this.orderNumber = orderNumber;
        this.title = title;
        this.content = content;
        this.deadLine = deadLine;
        this.createTime = createTime;
        this.visible = visible;
        Link = link;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
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

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Task copy(Program program, Topic topic, Task task) {
        Long paperId = 0L;
        return new Task(program.getId(), paperId, topic.getId(), task.getOrderNumber(), task.getTitle(), task.getContent(),
                task.getDeadLine(), task.getVisible(), task.getLink(), task.getType());
    }
}
