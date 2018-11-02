package cn.thoughtworks.school.programCenter.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tag")
@Setter
@Getter
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Date createTime;
    private Long creatorId;

    public Tag(String name, Date createTime, Long creatorId) {
        this.name = name;
        this.createTime = createTime;
        this.creatorId = creatorId;
    }
}
