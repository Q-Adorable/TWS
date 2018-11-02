package cn.thoughtworks.school.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "logicQuiz")
@Getter
@Setter
public class LogicQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(1)
    @Max(30)
    private int hardCount;

    @NotNull
    @Min(1)
    @Max(30)
    private int normalCount;

    @NotNull
    @Min(1)
    @Max(30)
    private int easyCount;

    @NotNull
    @Min(1)
    @Max(300)
    private int timeBoxInMinutes;
    private String description;
    private int exampleCount;
    private Date createTime;
    private Long quizGroupId;
    private Long makerId;
    private Boolean isAvailable;
}
