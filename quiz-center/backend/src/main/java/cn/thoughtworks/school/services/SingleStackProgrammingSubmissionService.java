package cn.thoughtworks.school.services;

import cn.thoughtworks.school.entities.HomeworkSubmission;
import cn.thoughtworks.school.repositories.HomeworkSubmissionRepository;
import cn.thoughtworks.school.requestParams.HomeworkSubmissionParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SingleStackProgrammingSubmissionService {

    @Autowired
    private HomeworkSubmissionRepository homeworkSubmissionRepository;


    public HomeworkSubmission save(HomeworkSubmissionParam param) {
        HomeworkSubmission homeworkSubmission = new HomeworkSubmission();
        homeworkSubmission.setAssignmentId(param.getAssignmentId());
        homeworkSubmission.setQuizId(param.getQuizId());
        homeworkSubmission.setUserId(param.getStudentId());
        homeworkSubmission.setAnswerBranch(param.getBranch());
        homeworkSubmission.setUserAnswer(param.getUserAnswerRepo());
        homeworkSubmission.setSubmitTime(new Date());
        homeworkSubmission.setResult("post to jenkins");
        return homeworkSubmissionRepository.save(homeworkSubmission);
    }

    public HomeworkSubmission save(HomeworkSubmission homeworkSubmission) {
        return homeworkSubmissionRepository.save(homeworkSubmission);
    }

    public HomeworkSubmission getSubmission(Long id) {

        return homeworkSubmissionRepository.getOne(id);
    }

    public List<HomeworkSubmission> getAllSubmission(Long userId, Long assignmentId, Long quizId, int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<HomeworkSubmission> all = homeworkSubmissionRepository.findByQuizIdAndAssignmentIdAndUserId(
                quizId, assignmentId, userId, pageable);
        return all.getContent();
    }

}
