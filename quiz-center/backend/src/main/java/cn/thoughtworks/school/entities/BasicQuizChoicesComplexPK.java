package cn.thoughtworks.school.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class BasicQuizChoicesComplexPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "[index]")
    private int index;

    private Long basicQuizId;

    public BasicQuizChoicesComplexPK() {
    }

    public BasicQuizChoicesComplexPK(int index, Long basicQuizId) {
        this.index = index;
        this.basicQuizId = basicQuizId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Long getBasicQuizId() {
        return basicQuizId;
    }

    public void setBasicQuizId(Long basicQuizId) {
        this.basicQuizId = basicQuizId;
    }
}
