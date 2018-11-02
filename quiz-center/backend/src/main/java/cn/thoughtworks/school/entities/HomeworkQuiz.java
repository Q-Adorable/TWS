package cn.thoughtworks.school.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "homeworkQuiz")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String evaluateScript;

    private String templateRepository;

    private Long makerId;

    private String homeworkName;

    private Date createTime;

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

    private String jobMessage;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "homeworkQuiz_tags",
            joinColumns = {@JoinColumn(name = "homeworkQuizId")},
            inverseJoinColumns = {@JoinColumn(name = "tagId")})
    private Set<Tag> tags = new HashSet<>();


}
