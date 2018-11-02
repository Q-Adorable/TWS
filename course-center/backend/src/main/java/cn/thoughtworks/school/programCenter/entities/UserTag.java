package cn.thoughtworks.school.programCenter.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "userTag")
@Setter
@Getter
@NoArgsConstructor
public class UserTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long studentId;
    private Date createTime;
    private Long operatorId;
    private Long programId;
    @OneToOne(cascade={CascadeType.PERSIST})
    @JoinColumn(name = "tagId")
    protected Tag tag;

    public UserTag(Long studentId, Date createTime, Long operatorId, Tag tag, Long programId) {
        this.studentId = studentId;
        this.createTime = createTime;
        this.operatorId = operatorId;
        this.tag = tag;
        this.programId = programId;
    }
}
