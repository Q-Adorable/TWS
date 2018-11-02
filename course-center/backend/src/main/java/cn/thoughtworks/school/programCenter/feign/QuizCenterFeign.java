package cn.thoughtworks.school.programCenter.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@FeignClient(name = "${feign.quizCenter.name}",
        url = "${feign.quizCenter.url}")
@Service
public interface QuizCenterFeign {

    @GetMapping("api/v3/basicQuizzes/ids")
    ResponseEntity<Map> getSelectingBasicQuizzes(@RequestParam("id") Long id, @RequestParam("page") int page);

    @GetMapping("api/v3/basicQuizzes/fuzzy")
    ResponseEntity<Map> fuzzySearchBasicQuizzes(@RequestParam("type") String type,
                                                @RequestParam("page") int page,
                                                @RequestParam("content") String content);

    @GetMapping("api/v3/basicQuizzes/selecting/{ids}")
    ResponseEntity<List> getBasicQuizzes(@PathVariable("ids") String ids);

    @GetMapping("api/v3/subjectiveQuizzes/selecting/{ids}")
    ResponseEntity<List> getSubjectiveQuizzes(@PathVariable("ids") String ids);

    @GetMapping("api/v3/homeworkQuizzes/ids")
    ResponseEntity<Map> getHomeworkQuizzesById(@RequestParam("id") Long id,
                                               @RequestParam("page") int page,
                                               @RequestParam("status") int status);

    @GetMapping("api/v3/basicQuizzes/fuzzy")
    ResponseEntity<Map> fuzzySearchHomework(@RequestParam("type") String type,
                                            @RequestParam("page") int page,
                                            @RequestParam("content") String content,
                                            @RequestParam("status") int status);

    @GetMapping("api/v3/onlineCodingQuizzes/ids")
    ResponseEntity<Map> getOnlineCodingQuizzesById(@RequestParam("id") Long id,
                                                   @RequestParam("page") int page,
                                                   @RequestParam("status") int status);

    @GetMapping("api/v3/onlineLanguageQuizzes/ids")
    ResponseEntity<Map> getOnlineLanguageQuizzesById(@RequestParam("id") Long id,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("status") int status);


    @GetMapping("api/v3/onlineLanguageQuizzes/fuzzy")
    ResponseEntity<Map> fuzzySearchOnlineLanguageQuizzes(@RequestParam("type") String type,
                                                         @RequestParam("page") int page,
                                                         @RequestParam("content") String content,
                                                         @RequestParam("status") int status);

    @GetMapping("api/v3/logicQuizzes/ids")
    ResponseEntity<Map> getLogicQuizzesById(@RequestParam("id") Long id,
                                            @RequestParam("page") int page);

    @GetMapping("api/v3/logicQuizzes/fuzzy")
    ResponseEntity<Map> fuzzySearchLogicQuizzes(@RequestParam("type") String type,
                                                @RequestParam("page") int page,
                                                @RequestParam("content") String content);

    @GetMapping("api/v3/logicQuizzes/selecting/{ids}")
    ResponseEntity<List> getLogicQuizzes(@PathVariable("ids") String ids);

    @GetMapping("api/v3/subjectiveQuizzes/ids")
    ResponseEntity<Map> getSubjectiveQuizzesById(@RequestParam("id") Long id,
                                                 @RequestParam("page") int page);

    @GetMapping("api/v3/subjectiveQuizzes/fuzzy")
    ResponseEntity<Map> fuzzySearchSubjectiveQuizzes(@RequestParam("type") String type,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("content") String content,
                                                     @RequestParam("status") int status);

    @GetMapping("api/v3/homeworkQuizzes/{ids}")
    ResponseEntity<List> getHomeworkQuizzesByIds(@PathVariable("ids") String ids,
                                                 @RequestParam("status") String status);

    @GetMapping("api/v3/onlineCodingQuizzes/{ids}")
    ResponseEntity<List> getOnlineCodingQuizzesByIds(@PathVariable("ids") String ids,
                                                     @RequestParam("status") String status);

    @GetMapping("api/v3/onlineLanguageQuizzes/{ids}")
    ResponseEntity<List> getOnlineLanguageQuizzesByIds(@PathVariable("ids") String ids,
                                                       @RequestParam("status") String status);

    @GetMapping("api/v3/homeworkQuizzes/students/{studentId}/assignments/{assignmentId}/quizzes/{ids}")
    ResponseEntity<List> getHomeworkWithUserAnswer(@PathVariable("studentId") Long studentId,
                                                   @PathVariable("assignmentId") Long assignmentId,
                                                   @PathVariable("ids") String ids);

    @GetMapping("api/v3/onlineCodingQuizzes/students/{studentId}/assignments/{assignmentId}/quizzes/{ids}")
    ResponseEntity<List> getOnlineCodingQuizzesWithUserAnswer(@PathVariable("studentId") Long studentId,
                                                              @PathVariable("assignmentId") Long assignmentId,
                                                              @PathVariable("ids") String ids);

    @GetMapping("api/v3/onlineLanguageQuizzes/students/{studentId}/assignments/{assignmentId}/quizzes/{ids}")
    ResponseEntity<List> getOnlineLanguageQuizzesWithUserAnswer(@PathVariable("studentId") Long studentId,
                                                                @PathVariable("assignmentId") Long assignmentId,
                                                                @PathVariable("ids") String ids);

    @GetMapping("api/v3/subjectiveQuizzes/students/{studentId}/assignments/{assignmentId}/quizzes/{ids}")
    ResponseEntity<List> getSubjectiveQuizzesWithUserAnswer(@PathVariable("studentId") Long studentId,
                                                            @PathVariable("assignmentId") Long assignmentId,
                                                            @PathVariable("ids") String ids);

    @GetMapping("api/v3/simpleCodingQuizzes/students/{studentId}/assignments/{assignmentId}/quizzes/{ids}")
    ResponseEntity<List> getSimpleCodingQuizzesWithUserAnswer(@PathVariable("studentId") Long studentId,
                                                              @PathVariable("assignmentId") Long assignmentId,
                                                              @PathVariable("ids") String ids);

    @PostMapping("api/v3/subjectiveQuizzes/students/{studentId}/assignments/{assignmentId}/quizzes/{quizId}")
    ResponseEntity<Map> submitSubjectiveAnswer(@PathVariable("studentId") Long studentId,
                                               @PathVariable("assignmentId") Long assignmentId,
                                               @PathVariable("quizId") Long quizId,
                                               @RequestBody Map data);

    @PostMapping("api/single-stack-programming-quiz-submissions")
    ResponseEntity<Map> createSubmission(@RequestBody Map data);

    @PostMapping("api/v3/onlineCodingSubmission")
    ResponseEntity<Map> judgeOnlineCodingQuiz(@RequestBody Map data);

    @PostMapping("api/single-language-online-coding-submissions")
    ResponseEntity<Map> addSubmission(@RequestBody Map data);

    @GetMapping("api/v3/homeworkSubmission/{id}/logs")
    ResponseEntity<Map> getSubmissionHomeworkLogBySubmissionId(@PathVariable("id") Long id);

    @GetMapping("api/v3/homeworkSubmission/users/{userId}/assignments/{assignmentId}/quizzess/{quizId}/logs")
    ResponseEntity<Map> getSubmissionHomeworkLogBySubmissionId(@PathVariable("assignmentId") Long assignmentId,
                                                               @PathVariable("quizId") Long quizId,
                                                               @PathVariable("userId") Long userId);

    @GetMapping("api/v3/onlineCodingSubmission/users/{id}/logs")
    ResponseEntity<Map> getSubmissionOnlineCodingLogBySubmissionId(@PathVariable("id") Long id);

    @GetMapping("api/v3/onlineCodingSubmission/users/{userId}/assignments/{assignmentId}/quizzess/{quizId}/logs")
    ResponseEntity<Map> getSubmissionOnlineCodingLogBySubmissionId(@PathVariable("assignmentId") Long assignmentId,
                                                                   @PathVariable("quizId") Long quizId,
                                                                   @PathVariable("userId") Long userId);

    @GetMapping("api/v3/onlineLanguageSubmission/users/{id}/logs")
    ResponseEntity<Map> getSubmissionOnlineLanguageLogBySubmissionId(@PathVariable("id") Long id);

    @GetMapping("api/v3/onlineLanguageSubmission/users/{userId}/assignments/{assignmentId}/quizzess/{quizId}/logs")
    ResponseEntity<Map> getSubmissionOnlineLanguageLogBySubmissionId(@PathVariable("assignmentId") Long assignmentId,
                                                                     @PathVariable("quizId") Long quizId,
                                                                     @PathVariable("userId") Long userId);

    @PostMapping("api/v3/basicQuizzes/students/{studentId}/assignments/{assignmentId}/quizzes")
    ResponseEntity<List> submitBasicQuiz(@PathVariable("studentId") Long studentId,
                                         @PathVariable("assignmentId") Long assignmentId,
                                         @RequestBody List<Map> data);

    @GetMapping("api/v3/basicQuizzes/students/{studentId}/assignments/{assignmentId}/quizzes/{ids}")
    ResponseEntity<List> getBasicQuizList(@PathVariable("studentId") Long studentId,
                                          @PathVariable("assignmentId") Long assignmentId,
                                          @PathVariable("ids") String ids);

    @GetMapping("api/v3/homeworkQuizzes/{ids}")
    ResponseEntity<Map> getCoding(@PathVariable("ids") String ids,
                                  @RequestParam(value = "status", defaultValue = "") String status);

    @GetMapping("api/v3/onlineCodingQuizzes/{ids}")
    ResponseEntity<Map> getOnlineCoding(@PathVariable("ids") String ids,
                                        @RequestParam(value = "status", defaultValue = "") String status);

    @GetMapping("api/v3/onlineLanguageQuizzes/{ids}")
    ResponseEntity<Map> getOnlineLanguageCoding(@PathVariable("ids") String ids,
                                                @RequestParam(value = "status", defaultValue = "") String status);

    @GetMapping("api/v3/homeworkQuizzes/{quizId}/answerFile")
    ResponseEntity getHomeworkQuizzes(@PathVariable("quizId") Long quizId);

    @GetMapping("api/v3/onlineCodingQuizzes/{id}/answerFile")
    ResponseEntity getOnlineCodingQuizzes(@PathVariable("id") Long id);

    @GetMapping("api/v3/onlineLanguageQuizzes/{quizId}/answerFile")
    ResponseEntity getOnlineLanguageQuizzes(@PathVariable("quizId") Long quizId);

    @GetMapping("api/v3/homeworkSubmission//oldAssignments/{oldAssignmentId}/quizzes/{quizId}/newAssignments/{newAssignmentId}")
    ResponseEntity homeworkDataMigration(@PathVariable("oldAssignmentId") Long oldAssignmentId,
                                         @PathVariable("quizId") Long quizId,
                                         @PathVariable("newAssignmentId") Long newAssignmentId);

    @GetMapping("api/v3/subjectiveQuizzes/oldAssignments/{oldAssignmentId}/quizzes/{quizId}/newAssignments/{newAssignmentId}")
    ResponseEntity subjectiveQuizDataMigration(@PathVariable("oldAssignmentId") Long oldAssignmentId,
                                               @PathVariable("quizId") Long quizId,
                                               @PathVariable("newAssignmentId") Long newAssignmentId);
}
