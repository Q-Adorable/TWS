package cn.thoughtworks.school.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "basicQuiz")
@ToString
@Setter
@Getter
public class BasicQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String type;
    private String answer;
    private boolean isAvailable;

    private Long quizGroupId;

    private String createTime;
    private String updateTime;
    private Long makerId;

    @MapsId("basicQuizId")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "basicQuizId")
    private List<BasicQuizChoices> choices;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "basicQuiz_tags",
            joinColumns = {@JoinColumn(name = "basicQuizId")},
            inverseJoinColumns = {@JoinColumn(name = "tagId")})
    private Set<Tag> tags = new HashSet<>();


    public BasicQuiz() {
    }

    public BasicQuiz(String description, String type, String answer) {
        this.description = description;
        this.type = type;
        this.answer = answer;
    }

}
