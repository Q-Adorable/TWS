package cn.thoughtworks.school.entities;

import javax.persistence.*;

@Entity
@Table(name = "practiseDiary")
public class PractiseDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String createTime;

    @Column
    private String date;

    @Column
    private String content;

    @Column
    private Long authorId;

    @Column
    private String diaryType;

    public PractiseDiary(Long id, String createTime, String date, String content, Long authorId) {
        this.id = id;
        this.createTime = createTime;
        this.date = date;
        this.content = content;
        this.authorId = authorId;
    }

    public PractiseDiary(String createTime, String date, String content, Long authorId) {
        this.createTime = createTime;
        this.date = date;
        this.content = content;
        this.authorId = authorId;
    }

    public PractiseDiary(String createTime, String date, String content, Long authorId, String diaryType) {
        this.createTime = createTime;
        this.date = date;
        this.content = content;
        this.authorId = authorId;
        this.diaryType = diaryType;
    }

    PractiseDiary() {

    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getId() {
        return id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String toString() {
        return this.content;
    }

    public String getDiaryType() {
        return diaryType;
    }

    public void setDiaryType(String diaryType) {
        this.diaryType = diaryType;
    }
}
