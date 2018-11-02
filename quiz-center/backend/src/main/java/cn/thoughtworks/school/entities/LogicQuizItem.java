package cn.thoughtworks.school.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Entity
@Table(name = "logicQuizItem")
@Setter
@Getter
public class LogicQuizItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String initializedBox;
    private String stepsString;
    private int count;
    private String questionZh;
    private int stepsLength;
    private int maxUpdateTimes;

    @JsonIgnore
    private String answer;

    private String descriptionZh;

    private String chartPath;

    private String infoPath;

    @Transient
    private String exampleAnswer;

    @Transient
    private Boolean isExample;

}
