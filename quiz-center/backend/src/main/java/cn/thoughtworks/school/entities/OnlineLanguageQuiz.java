package cn.thoughtworks.school.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "onlineLanguageQuiz")
@Setter
@Getter
public class OnlineLanguageQuiz {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String description;

  private String testData;

  private String initCode;

  private Long makerId;

  private String onlineLanguageName;

  private String language;

  private Date createTime;

  private String answer;

  private Long stackId;

  private Long rawId;

  private String answerDescription;

  private Date updateTime;

  private boolean isAvailable;

  private int status;
  
  private String remark;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "onlineLanguageQuiz_tags",
    joinColumns = {@JoinColumn(name = "onlineLanguageQuizId")},
    inverseJoinColumns = {@JoinColumn(name = "tagId")})
  private Set<Tag> tags = new HashSet<>();


  public OnlineLanguageQuiz() {
  }

  public OnlineLanguageQuiz(String description, String testData, String initCode, Long makerId, String onlineLanguageName, String language, Date createTime, String answer, Long rawId, String answerDescription) {
    this.description = description;
    this.testData = testData;
    this.initCode = initCode;
    this.makerId = makerId;
    this.onlineLanguageName = onlineLanguageName;
    this.language = language;
    this.createTime = createTime;
    this.answer = answer;
    this.rawId = rawId;
    this.answerDescription = answerDescription;
  }

}
