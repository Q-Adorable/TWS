package cn.thoughtworks.school.programCenter.services;

import cn.thoughtworks.school.programCenter.feign.QuizCenterFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QuizCenterService {
    private int SUCCESS_STATUS = 2;
    @Value("${quizCenter}")
    private String quizCenter;
    private RestTemplate template = new RestTemplate();

    @Autowired
    private QuizCenterFeign quizCenterFeign;

    public Map getBasicQuizzes(Integer page, String searchType, String searchContent) {
        if (("quizId").equals(searchType)) {
            return quizCenterFeign.getSelectingBasicQuizzes(Long.parseLong(searchContent), page).getBody();
        }
        if (searchType.equals("description")) {
            return quizCenterFeign.fuzzySearchBasicQuizzes(searchType, page, searchContent).getBody();
        }

        String getQuizzesUrl = quizCenter + "/api/v3/basicQuizzes?page=" + page + "&" + searchType + "=" + searchContent;
        return template.getForEntity(getQuizzesUrl, Map.class).getBody();
    }

    public List getSelectingBasicQuizzes(String ids) {
        return quizCenterFeign.getBasicQuizzes(ids).getBody();
    }

    public Map getHomeworkQuizzes(Integer page, String searchType, String searchContent) {
        if (("quizId").equals(searchType)) {
            return quizCenterFeign.getHomeworkQuizzesById(Long.parseLong(searchContent), page, SUCCESS_STATUS).getBody();
        }
        if (searchType.equals("remark") || searchType.equals("description")) {
            return quizCenterFeign.fuzzySearchHomework(searchType, page, searchContent, SUCCESS_STATUS).getBody();
        }

        String getQuizzesUrl = quizCenter + "/api/v3/homeworkQuizzes?page=" + page + "&" + searchType + "=" + searchContent + "&status=" + SUCCESS_STATUS;
        return template.getForEntity(getQuizzesUrl, Map.class).getBody();
    }

    public Map getOnlineCodingQuizzes(Integer page, String searchType, String searchContent) {
        if (("quizId").equals(searchType)) {
            return quizCenterFeign.getOnlineCodingQuizzesById(Long.parseLong(searchContent), page, SUCCESS_STATUS).getBody();
        }

        String getQuizzesUrl = quizCenter + "/api/v3/onlineCodingQuizzes?page=" + page + "&" + searchType + "=" + searchContent + "&status=" + SUCCESS_STATUS;
        return template.getForEntity(getQuizzesUrl, Map.class).getBody();
    }

    public Map getOnlineLanguageQuizzes(Integer page, String searchType, String searchContent) {
        if (("quizId").equals(searchType)) {
            return quizCenterFeign.getOnlineLanguageQuizzesById(Long.parseLong(searchContent), page, SUCCESS_STATUS).getBody();
        }
        if (searchType.equals("remark") || searchType.equals("description")) {
            return quizCenterFeign.fuzzySearchOnlineLanguageQuizzes(searchType, page, searchContent, SUCCESS_STATUS).getBody();
        }

        String getQuizzesUrl = quizCenter + "/api/v3/onlineLanguageQuizzes?page=" + page + "&" + searchType + "=" + searchContent + "&status=" + SUCCESS_STATUS;
        return template.getForEntity(getQuizzesUrl, Map.class).getBody();
    }

    public List getSelectHomeworkQuizzes(String ids) {
        return quizCenterFeign.getHomeworkQuizzesByIds(ids, "many").getBody();
    }

    public List getSelectOnlineCodingQuizzes(String ids) {
        return quizCenterFeign.getOnlineCodingQuizzesByIds(ids, "many").getBody();
    }

    public List getSelectOnlineLanguageQuizzes(String ids) {
        return quizCenterFeign.getOnlineLanguageQuizzesByIds(ids, "many").getBody();
    }

    public List getSelectHomeworkWithUserAnswer(String ids, Long assignmentId, Long studentId) {
        return quizCenterFeign.getHomeworkWithUserAnswer(studentId, assignmentId, ids).getBody();
    }

    public List getSelectOnlineCodingWithUserAnswer(String ids, Long assignmentId, Long studentId) {
        return quizCenterFeign.getHomeworkWithUserAnswer(studentId, assignmentId, ids).getBody();
    }

    public List getSelectOnlineLanguageWithUserAnswer(String ids, Long assignmentId, Long studentId) {
        return quizCenterFeign.getOnlineLanguageQuizzesWithUserAnswer(studentId, assignmentId, ids).getBody();
    }

    public Map getSubjectiveQuizzes(Integer page, String searchType, String searchContent) {
        if (("quizId").equals(searchType)) {
            return quizCenterFeign.getSubjectiveQuizzesById(Long.parseLong(searchContent), page).getBody();
        }
        if (searchType.equals("remark") || searchType.equals("description")) {
            return quizCenterFeign.fuzzySearchSubjectiveQuizzes(searchType, page, searchContent, SUCCESS_STATUS).getBody();
        }

        String getQuizzesUrl = quizCenter + "/api/v3/subjectiveQuizzes?page=" + page + "&" + searchType + "=" + searchContent;
        return template.getForEntity(getQuizzesUrl, Map.class).getBody();
    }

    public List getSelectSubjectiveQuizzes(String ids) {
        return quizCenterFeign.getSubjectiveQuizzes(ids).getBody();
    }

    public Map getLogicQuizzes(Integer page, String searchType, String searchContent) {
        if (("quizId").equals(searchType)) {
            return quizCenterFeign.getLogicQuizzesById(Long.parseLong(searchContent), page).getBody();
        } else if (searchType.equals("description")) {
            return quizCenterFeign.fuzzySearchLogicQuizzes(searchType, page, searchContent).getBody();
        }

        String getQuizzesUrl = quizCenter + "/api/v3/logicQuizzes?page=" + page + "&" + searchType + "=" + searchContent;
        return template.getForEntity(getQuizzesUrl, Map.class).getBody();
    }

    public List getSelectLogicQuizzes(String ids) {
        return quizCenterFeign.getLogicQuizzes(ids).getBody();
    }

    public ResponseEntity<List> getTags() {
        String getTagsUrl = quizCenter + "/api/v3/tags/searchTags";

        ResponseEntity<List> result = template.getForEntity(getTagsUrl, List.class);
        return result;
    }

    public List getSelectSubjectiveQuizzesWithUserAnswer(String ids, Long assignmentId, Long studentId) {
        return quizCenterFeign.getSubjectiveQuizzesWithUserAnswer(studentId, assignmentId, ids).getBody();
    }

    public List getSelectSimpleCodingQuizzesWithUserAnswer(String ids, Long assignmentId, Long studentId) {
        return quizCenterFeign.getSimpleCodingQuizzesWithUserAnswer(studentId, assignmentId, ids).getBody();
    }

    public ResponseEntity<Map> submitSubjectiveAnswer(Long assignmentId, Long quizId, Long studentId, Map data) {
        return quizCenterFeign.submitSubjectiveAnswer(studentId, assignmentId, quizId, data);
    }

    public ResponseEntity<Map> submitHomeworkQuizAnswer(Map data) {
        return quizCenterFeign.createSubmission(data);
    }

    public ResponseEntity<Map> submitOnlineCodingQuizAnswer(Map data) {
        return quizCenterFeign.judgeOnlineCodingQuiz(data);
    }

    /**
     * TODO: today
     */
    public ResponseEntity<Map> submitOnlineLanguageQuizAnswer(Map data) {
        return quizCenterFeign.addSubmission(data);
    }

    public Map getHomeworkQuizzesLogs(Long submissionId, Long assignmentId, Long quizId, Long userId) {
        if (submissionId == 0) {
            return quizCenterFeign.getSubmissionHomeworkLogBySubmissionId(assignmentId, quizId, userId).getBody();
        }
        return quizCenterFeign.getSubmissionHomeworkLogBySubmissionId(submissionId).getBody();
    }

    public Map getOnlineCodingQuizzesLogs(Long submissionId, Long assignmentId, Long quizId, Long userId) {
        if (submissionId == 0) {
            return quizCenterFeign.getSubmissionHomeworkLogBySubmissionId(userId, assignmentId, quizId).getBody();
        }
        return quizCenterFeign.getSubmissionHomeworkLogBySubmissionId(submissionId).getBody();
    }

    public Map getOnlineLanguageQuizzesLogs(Long submissionId, Long assignmentId, Long quizId, Long userId) {
        if (submissionId == 0) {
            return quizCenterFeign.getSubmissionOnlineLanguageLogBySubmissionId(userId, assignmentId, quizId).getBody();
        }
        return quizCenterFeign.getSubmissionHomeworkLogBySubmissionId(submissionId).getBody();
    }

    public List submitBasicQuizAnswer(Long assignmentId, List<Map> answer, Long studentId) {
        return quizCenterFeign.submitBasicQuiz(studentId, assignmentId, answer).getBody();
    }

    public List getSelectingBasicQuizzesWithUserAnswer(String ids, Long assignmentId, Long studentId) {
        return quizCenterFeign.getBasicQuizList(studentId, assignmentId, ids).getBody();
    }

    public Map getHomeworkQuizById(Long quizId) {
        return quizCenterFeign.getCoding(quizId.toString(), "").getBody();
    }

    public Map getOnlineCodingQuizById(Long quizId) {
        return quizCenterFeign.getOnlineCoding(quizId.toString(), "").getBody();
    }

    public Map getOnlineLanguageQuizById(Long quizId) {
        return quizCenterFeign.getOnlineLanguageCoding(quizId.toString(), "").getBody();
    }

    public ResponseEntity getHomeworkAnswerFile(Long quizId) {
        return quizCenterFeign.getHomeworkQuizzes(quizId);
    }

    public ResponseEntity getOnlineCodingAnswerFile(Long quizId) {
        return quizCenterFeign.getOnlineCodingQuizzes(quizId);
    }

    public ResponseEntity getOnlineLanguageAnswerFile(Long quizId) {
        return quizCenterFeign.getOnlineLanguageQuizzes(quizId);
    }

    public List getQuizzesAndAnswerByQuizIds(String ids, Long assignmentId, Long studentId, String type) {
        List quizzes = new ArrayList();
        if (ids.equals("")) {
            return quizzes;
        }
        if (type.equals("BASIC_QUIZ")) {
            quizzes = this.getSelectingBasicQuizzesWithUserAnswer(ids, assignmentId, studentId);
        } else if ("HOMEWORK_QUIZ".equals(type)) {
            quizzes = this.getSelectHomeworkWithUserAnswer(ids, assignmentId, studentId);
        } else if ("ONLINE_CODING_QUIZ".equals(type)) {
            quizzes = this.getSelectOnlineCodingWithUserAnswer(ids, assignmentId, studentId);
        } else if ("ONLINE_LANGUAGE_QUIZ".equals(type)) {
            quizzes = this.getSelectOnlineLanguageWithUserAnswer(ids, assignmentId, studentId);
        } else if ("SUBJECTIVE_QUIZ".equals(type)) {
            quizzes = this.getSelectSubjectiveQuizzesWithUserAnswer(ids, assignmentId, studentId);
        } else if ("SIMPLE_CODING_QUIZ".equals(type)) {
            quizzes = this.getSelectSimpleCodingQuizzesWithUserAnswer(ids, assignmentId, studentId);
        }
        return quizzes;
    }

    public void dataMigration(Long oldAssignmentId, Long quizId, Long newAssignmentId, String type) {


        if ("HOMEWORK_QUIZ".equals(type)) {
            this.homeworkDataMigration(oldAssignmentId, quizId, newAssignmentId);
        } else if ("SUBJECTIVE_QUIZ".equals(type)) {
            this.subjectiveQuizzesDataMigration(oldAssignmentId, quizId, newAssignmentId);
        }
    }

    public ResponseEntity homeworkDataMigration(Long oldAssignmentId, Long quizId, Long newAssignmentId) {
        return quizCenterFeign.homeworkDataMigration(oldAssignmentId, quizId, newAssignmentId);
    }

    public ResponseEntity subjectiveQuizzesDataMigration(Long oldAssignmentId, Long quizId, Long newAssignmentId) {
        return quizCenterFeign.subjectiveQuizDataMigration(oldAssignmentId, quizId, newAssignmentId);
    }

    public Map getSubmissionByAssignmentIdAndQuizId(Long userId, Long assignmentId, Long quizId) {
        String url = quizCenter + "/api/simpleCodingSubmissions/users/" + userId + "/assignments/" + assignmentId + "/quizzes/" + quizId;
        ResponseEntity<Map> result = template.getForEntity(url, Map.class);
        return result.getBody();
    }

    public ResponseEntity getSimpleCodingAnswerFile(Long quizId) {
        String url = quizCenter + "/api/simpleCodingQuizzes/" + quizId + "/answerFile";
        return template.getForEntity(url, String.class);
    }

    public Map getSimpleCodingQuizzes(Integer page, String searchType, String searchContent) {
        String getQuizzesUrl;
        if (("quizId").equals(searchType)) {
            getQuizzesUrl = quizCenter + "/api/simpleCodingQuizzes/ids?page=" + page + "&id=" + searchContent;
        } else {
            getQuizzesUrl = quizCenter + "/api/simpleCodingQuizzes?page=" + page + "&" + searchType + "=" + searchContent;
        }
        ResponseEntity<Map> result = template.getForEntity(getQuizzesUrl, Map.class);
        return result.getBody();
    }
}
