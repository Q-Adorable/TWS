package cn.thoughtworks.school.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TWLogicQuizSubmission")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TWLogicQuizSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long assignmentId;

    private Long userId;

    private Long quizId;

    private Date submitTime;

    private Date startTime;

    private String status;

    @OneToMany(cascade = CascadeType.PERSIST, fetch=FetchType.EAGER )
    @JoinColumn(name = "submissionId")
    private List<TWLogicQuizItemAnswer> TWLogicQuizItemAnswer;

    @Transient
    private List<TWLogicQuizItemAnswer> TWExampleLogicQuizItemAnswer;
}
