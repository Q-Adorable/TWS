package cn.thoughtworks.school.requestParams;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author yywei
 **/
@Getter
@Setter
public class SingleLanguageSubmissionParam {
    private String userAnswerCode;
    private String language;
    private Long id;
    private Long assignmentId;
    private Long studentId;

}
