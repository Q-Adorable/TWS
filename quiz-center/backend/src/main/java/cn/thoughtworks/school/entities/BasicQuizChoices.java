package cn.thoughtworks.school.entities;

import javax.persistence.*;

@Entity
@Table(name = "basicQuizChoices")
public class BasicQuizChoices {
    @EmbeddedId
    private BasicQuizChoicesComplexPK complexPK;
    private String choice;

    public BasicQuizChoices() {
    }

    public BasicQuizChoices(BasicQuizChoicesComplexPK complexPK, String choice) {
        this.complexPK = complexPK;
        this.choice = choice;
    }

    public BasicQuizChoicesComplexPK getComplexPK() {
        return complexPK;
    }

    public void setComplexPK(BasicQuizChoicesComplexPK complexPK) {
        this.complexPK = complexPK;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
