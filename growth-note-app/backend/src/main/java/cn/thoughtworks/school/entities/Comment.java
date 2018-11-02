package cn.thoughtworks.school.entities;

import javax.persistence.*;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String commentTime;

    @Column
    private String commentContent;

    @Column
    private Long practiseDiaryId;

    @Column
    private Long commentAuthorId;


    public Comment( String commentContent, Long practiseDiaryId, Long commentAuthorId) {
        this.commentContent = commentContent;
        this.practiseDiaryId = practiseDiaryId;
        this.commentAuthorId = commentAuthorId;
    }

    public Comment() {}

    public Long getCommentAuthorId() {
        return commentAuthorId;
    }

    public Long getPractiseDiaryId() {

        return practiseDiaryId;
    }

    public String getCommentContent() {

        return commentContent;
    }

    public String getCommentTime() {

        return commentTime;
    }

    public Long getId() {

        return id;
    }


    public void setCommentAuthorId(Long commentAuthorId) {
        this.commentAuthorId = commentAuthorId;
    }

    public void setPractiseDiaryId(Long practiseDiaryId) {

        this.practiseDiaryId = practiseDiaryId;
    }

    public void setCommentContent(String commentContent) {

        this.commentContent = commentContent;
    }

    public void setCommentTime(String commentTime) {

        this.commentTime = commentTime;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String toString() {
        return this.commentContent;
    }

}
