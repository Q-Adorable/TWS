package cn.thoughtworks.school.requestParams;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SubmitUserAnswerParam {
    private Long userId;
    private Long assignmentId;
    private List<Map<String, Integer>> answers;
}