package cn.thoughtworks.school.entities;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class ResultBasicQuiz {
  private Long id;
  private String description;
  private String type;
  private String answer;
  private List<String> options;
  private List choices;
  private Date createTime;
  private Date updateTime;
  private List<String> tags;
  private Long makerId;

  public ResultBasicQuiz() {
  }

  public ResultBasicQuiz(Long id, String description, String type, String answer) {
    this.id = id;
    this.description = description;
    this.type = type;
    this.answer = answer;
  }

  public Long getMakerId() {
    return makerId;
  }

  public void setMakerId(Long makerId) {
    this.makerId = makerId;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public List<String> getOptions() {
    return options;
  }

  public void setOptions(List<String> options) {
    this.options = options;
  }

  public List getChoices() {
    return choices;
  }

  public void setChoices(List choices) {
    this.choices = choices;
  }
}
