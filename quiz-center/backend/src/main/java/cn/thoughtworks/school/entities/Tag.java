package cn.thoughtworks.school.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tag")
public class Tag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private Long makerId;
  private String createTime;
  private String updateTime;
  private Long referenceNumber;

  @ManyToMany(fetch = FetchType.LAZY,
      cascade =CascadeType.ALL,
      mappedBy = "tags")
  @JsonBackReference
  private Set<SubjectiveQuiz> subjectiveQuizzes = new HashSet<>();

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "basicQuiz_tags",
    inverseJoinColumns = {@JoinColumn(name = "basicQuizId")},
    joinColumns = {@JoinColumn(name = "tagId")})
  @JsonBackReference
  private Set<BasicQuiz> basicQuizzes = new HashSet<>();

  public Tag(String name, Long makerId, Long referenceNumber) {
    this.name = name;
    this.makerId = makerId;
    this.referenceNumber = referenceNumber;
  }

  public Tag() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getMakerId() {
    return makerId;
  }

  public void setMakerId(Long makerId) {
    this.makerId = makerId;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  public Long getReferenceNumber() {
    return referenceNumber;
  }

  public void setReferenceNumber(Long referenceNumber) {
    this.referenceNumber = referenceNumber;
  }

  public Set<SubjectiveQuiz> getSubjectiveQuizzes() {
    return subjectiveQuizzes;
  }

  public void setSubjectiveQuizzes(Set<SubjectiveQuiz> subjectiveQuizzes) {
    this.subjectiveQuizzes = subjectiveQuizzes;
  }

  public Set<BasicQuiz> getBasicQuizzes() {
    return basicQuizzes;
  }

  public void setBasicQuizzes(Set<BasicQuiz> basicQuizzes) {
    this.basicQuizzes = basicQuizzes;
  }
}
