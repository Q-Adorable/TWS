package cn.thoughtworks.school.entities;

import javax.persistence.*;

@Entity
@Table(name = "excellentDiary")
public class ExcellentDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String createTime;

    @Column
    private Long diaryId;

    @Column
    private Long operatorId;

    ExcellentDiary() {
    }

    public ExcellentDiary(String createTime, Long diaryId, Long operatorId) {
        this.createTime = createTime;
        this.diaryId = diaryId;
        this.operatorId = operatorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(Long diaryId) {
        this.diaryId = diaryId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }
}
