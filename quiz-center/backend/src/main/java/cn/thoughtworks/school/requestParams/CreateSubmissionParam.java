package cn.thoughtworks.school.requestParams;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class CreateSubmissionParam {
    private Long userId;

    @Min(1)
    private Long assignmentId;

    @Min(1)
    private Long logicQuizId;
}
