package cn.thoughtworks.school.entities;

import lombok.Getter;

import lombok.NoArgsConstructor;

import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "onlineCodingQuiz")

@Setter
@Getter
@NoArgsConstructor

public class OnlineCodingQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String evaluateScript;

    private String templateRepository;

    private Long makerId;

    private String onlineCodingName;

    private String language;

    private int createTime;

    private String answerPath;

    private Long stackId;

    private Long rawId;

    private String answerDescription;

    private String definitionRepo;

    private Date updateTime;

    private boolean isAvailable;

    private int status;

    private Long buildNumber;
    private Long quizGroupId;

    private String remark;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "onlineCodingQuiz_tags",
            joinColumns = {@JoinColumn(name = "onlineCodingQuizId")},
            inverseJoinColumns = {@JoinColumn(name = "tagId")})
    private Set<Tag> tags = new HashSet<>();

    public OnlineCodingQuiz(String description, String evaluateScript, String templateRepository, Long makerId, String onlineCodingName, String language, Integer createTime, String answerPath, Long stackId, Long rawId, String answerDescription, String definitionRepo) {

        this.description = description;
        this.evaluateScript = evaluateScript;
        this.templateRepository = templateRepository;
        this.makerId = makerId;
        this.onlineCodingName = onlineCodingName;
        this.language = language;
        this.createTime = createTime;
        this.answerPath = answerPath;
        this.stackId = stackId;
        this.rawId = rawId;
        this.answerDescription = answerDescription;
        this.definitionRepo = definitionRepo;
    }

}
