package cn.thoughtworks.school.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "quizGroup")
@Getter
@Setter
@ToString
public class QuizGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String createTime;
    private String modifyTime;
    private Long makerId;

    public QuizGroup() {
    }

    public QuizGroup(String name, String description, String createTime, Long makerId) {
        this.name = name;
        this.description = description;
        this.createTime = createTime;
        this.makerId = makerId;
    }
}
