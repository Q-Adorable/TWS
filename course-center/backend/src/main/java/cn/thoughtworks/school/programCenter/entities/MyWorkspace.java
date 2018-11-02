package cn.thoughtworks.school.programCenter.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "myWorkspace")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyWorkspace {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long userId;
  private Long programId;
  private Date createTime;

  public MyWorkspace(Long userId, Long programId, Date date) {
    this.userId = userId;
    this.programId = programId;
    this.createTime = date;
  }
}