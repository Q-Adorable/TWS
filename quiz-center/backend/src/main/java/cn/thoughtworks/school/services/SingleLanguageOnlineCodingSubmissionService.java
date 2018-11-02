package cn.thoughtworks.school.services;

import cn.thoughtworks.school.entities.OnlineLanguageSubmission;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.OnlineLanguageSubmissionRepository;
import cn.thoughtworks.school.requestParams.SingleLanguageSubmissionParam;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author yywei
 **/
@Service
public class SingleLanguageOnlineCodingSubmissionService {
    @Autowired
    OnlineLanguageSubmissionRepository onlineLanguageSubmissionRepository;

    public OnlineLanguageSubmission add(SingleLanguageSubmissionParam singleLanguageSubmissionParam) {
        OnlineLanguageSubmission onlineLanguageSubmission = new OnlineLanguageSubmission();
        onlineLanguageSubmission.setQuizId(singleLanguageSubmissionParam.getId());
        onlineLanguageSubmission.setAssignmentId(singleLanguageSubmissionParam.getAssignmentId());
        onlineLanguageSubmission.setUserId(singleLanguageSubmissionParam.getStudentId());
        onlineLanguageSubmission.setAnswerLanguage(singleLanguageSubmissionParam.getLanguage());
        onlineLanguageSubmission.setUserAnswer(singleLanguageSubmissionParam.getUserAnswerCode());
        onlineLanguageSubmission.setSubmitTime(new Date());
        onlineLanguageSubmission.setStatus(-1);
        onlineLanguageSubmission.setResult(wrapperResultToJSONArray("判题中.", "", onlineLanguageSubmission));
        return onlineLanguageSubmissionRepository.save(onlineLanguageSubmission);
    }

    public OnlineLanguageSubmission add(OnlineLanguageSubmission onlineLanguageSubmission) {
        return onlineLanguageSubmissionRepository.save(onlineLanguageSubmission);
    }

    public OnlineLanguageSubmission get(Long id) throws BusinessException {
        return onlineLanguageSubmissionRepository.findById(id).orElseThrow(
                () -> new BusinessException(String.format("Unknown basicQuiz with id: %s", id)));
    }

    public List<OnlineLanguageSubmission> getList(Long userId, Long assignmentId, Long quizId, int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<OnlineLanguageSubmission> pages = onlineLanguageSubmissionRepository.
                findByUserIdAndAssignmentIdAndQuizId(userId, assignmentId, quizId, pageable);
        return pages.getContent();
    }

    public void updateCallback(Long id, Integer status, String msg, String job_msg) throws BusinessException {
        OnlineLanguageSubmission onlineLanguageSubmission = get(id);
        onlineLanguageSubmission.setStatus(status);
        onlineLanguageSubmission.setResult(wrapperResultToJSONArray(msg, job_msg, onlineLanguageSubmission));
        add(onlineLanguageSubmission);
    }

    private String wrapperResultToJSONArray(String msg, String job_msg, OnlineLanguageSubmission onlineLanguageSubmission) {
        String result = onlineLanguageSubmission.getResult();
        JSONArray jsonArray;
        if (result == null || result.isEmpty()) {
            jsonArray = new JSONArray();
        } else {
            jsonArray = new JSONArray(result);
        }
        Map<String, String> map = new LinkedHashMap<>(3);
        map.put("time", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        map.put("message", msg);
        map.put("job_msg", job_msg);
        jsonArray.put(map);
        return jsonArray.toString();
    }
}
