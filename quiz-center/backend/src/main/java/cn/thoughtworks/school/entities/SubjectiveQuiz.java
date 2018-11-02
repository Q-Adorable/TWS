package cn.thoughtworks.school.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "subjectiveQuiz")

@Setter
@Getter

public class SubjectiveQuiz {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String description;
  private Long makerId;
  private Long referenceNumber;
  private Boolean isAvailable;
  private Long quizGroupId;
  private String createTime;
  private String updateTime;
  private String remark;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "subjectiveQuiz_tags",
      joinColumns = {@JoinColumn(name = "subjectiveQuizId")},
      inverseJoinColumns = {@JoinColumn(name = "tagId")})
  @JsonManagedReference
  private Set<Tag> tags = new HashSet<>();

  public SubjectiveQuiz() {
  }

  public SubjectiveQuiz(String description, Long referenceNumber, Boolean isAvailable) {
    this.description = description;
    this.referenceNumber = referenceNumber;
    this.isAvailable = isAvailable;
  }

}
