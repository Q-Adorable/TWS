package cn.thoughtworks.school.entities;

import javax.persistence.*;

@Entity
@Table(name = "stack")
public class Stack {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "stackId")
  private Long Id;
  private String description;

  @Column(name = "definition")
  private String image;

  private String createTime;
  private Boolean isAvailable;
  private Long referenceNumber;
  private Long makerId;
  private Long buildNumber;

  private String title;

  public Stack() {
  }

  public Stack(String description, String definition, Long referenceNumber) {
    this.description = description;
    this.image = definition;
    this.referenceNumber = referenceNumber;
  }

  public Long getBuildNumber() {
    return buildNumber;
  }

  public void setBuildNumber(Long buildNumber) {
    this.buildNumber = buildNumber;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setAvailable(Boolean available) {
    isAvailable = available;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public Boolean getAvailable() {
    return isAvailable;
  }

  public Long getReferenceNumber() {
    return referenceNumber;
  }

  public void setReferenceNumber(Long referenceNumber) {
    this.referenceNumber = referenceNumber;
  }

  public Long getMakerId() {
    return makerId;
  }

  public void setMakerId(Long makerId) {
    this.makerId = makerId;
  }

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
