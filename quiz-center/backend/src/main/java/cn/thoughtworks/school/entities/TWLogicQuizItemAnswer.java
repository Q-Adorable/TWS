package cn.thoughtworks.school.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "TWLogicQuizItemAnswer")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TWLogicQuizItemAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long submissionId;

    private String answer;

    private Integer quizItemId;

    @JsonIgnore
    private Boolean isCorrect;
}
