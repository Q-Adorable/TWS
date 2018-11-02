package cn.thoughtworks.school.requestParams;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateHomeworkParam {

    private String definitionRepo;
    private String title;
    private List<String> tags;
    private Long stackId;
    private String remark;
    private Long quizGroupId;
    private Long makerId;
}
