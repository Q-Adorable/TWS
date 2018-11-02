package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.annotations.Auth;
import cn.thoughtworks.school.programCenter.entities.*;
import cn.thoughtworks.school.programCenter.exceptions.BusinessException;
import cn.thoughtworks.school.programCenter.repositories.*;
import cn.thoughtworks.school.programCenter.services.AssignmentService;
import cn.thoughtworks.school.programCenter.services.QuizCenterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v2")
public class AssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private QuizCenterService quizCenterService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private AssignmentQuizRepository assignmentQuizRepository;
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ReviewQuizRepository reviewQuizRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ExcellentQuizCommentRepository excellentQuizCommentRepository;
    @Autowired
    private QuizSuggestionRepository quizSuggestionRepository;

    @Autowired
    private ProgramRepository programRepository;
    private ObjectMapper oMapper = new ObjectMapper();

    @RequestMapping(value = "/assignments", method = RequestMethod.POST)
    public ResponseEntity addAssignment(@RequestBody Assignment assignment, @Auth User current) {
        assignment.setCreatorId(current.getId());
        Assignment newAssignment = assignmentRepository.save(assignment);
        HashMap data = new HashMap();
        data.put("uri", "/api/v2/assignments/" + newAssignment.getId());
        data.put("id", newAssignment.getId());
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/tasks/{taskId}/assignments", method = RequestMethod.GET)
    public ResponseEntity getAssignments(@PathVariable Long taskId, @Auth User current) {
        List<Assignment> assignmentList = assignmentRepository.findByTaskIdOrderById(taskId);
        List assignments = getAssignments(current.getId(), assignmentList);
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }

    @RequestMapping(value = "/tutor/students/{studentId}/tasks/{taskId}/assignments", method = RequestMethod.GET)
    public ResponseEntity tutorGetAssignments(@PathVariable Long taskId, @PathVariable Long studentId) throws Exception {
        List<Assignment> assignmentList = assignmentRepository.findByTaskIdOrderByIdDesc(taskId);
        List assignments = getAssignments(studentId, assignmentList);
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }

    private List getAssignments(Long studentId, List<Assignment> assignmentList) {
        return assignmentList.stream()
                .filter((Assignment assignment) ->
                        assignment.getSelectedQuizzes().size() > 0)
                .map((Assignment assignment) -> {
                    int assignmentQuizCount = assignment.getSelectedQuizzes().size();
                    List<ReviewQuiz> assignmentQuizList = reviewQuizRepository.findByAssignmentIdAndStudentId(assignment.getId(), studentId);
                    Map map = oMapper.convertValue(assignment, Map.class);
                    String status;
                    if (assignment.getType().equals("BASIC_QUIZ")) {
                        status = assignmentQuizList.size() == 0 ? "未提交" : "已完成";
                    } else {
                        status = getFinishCount(assignmentQuizList, assignmentQuizCount);
                    }
                    map.put("status", status);
                    return map;
                }).collect(Collectors.toList());
    }

    private String getFinishCount(List<ReviewQuiz> list, int assignmentQuizCount) {
        int finished = list.stream().filter(item -> "已完成".equals(item.getStatus())).collect(Collectors.toList()).size();
        int excellent = list.stream().filter(item -> "优秀".equals(item.getStatus())).collect(Collectors.toList()).size();
        return "已完成 " + (finished + excellent) + " / " + assignmentQuizCount;
    }

    @RequestMapping(value = "/assignments/quizzesByIds", method = RequestMethod.GET)
    public ResponseEntity getQuizzesByIds(
            @RequestParam(value = "type", defaultValue = "") String type,
            @RequestParam(value = "ids", defaultValue = "") String ids) {
        List quizzes = getQuizzesByQuizIds(ids, type);

        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @RequestMapping(value = "/assignments/{assignmentId}", method = RequestMethod.GET)
    public ResponseEntity getAssignment(@PathVariable Long assignmentId) throws BusinessException {
        Assignment assignment = assignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new BusinessException(String.format("Unknown assignment with id: %s", assignmentId)));
        return new ResponseEntity<>(assignment, HttpStatus.OK);
    }

    @RequestMapping(value = "/assignments/{assignmentId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteAssignment(@PathVariable Long assignmentId) throws BusinessException {
        assignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new BusinessException(String.format("Unknown assignment with id: %s", assignmentId)));

        assignmentQuizRepository.deleteAllByAssignmentId(assignmentId);
        assignmentRepository.deleteById(assignmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/assignments/{assignmentId}", method = RequestMethod.PUT)
    public ResponseEntity getAssignment(@PathVariable Long assignmentId, @RequestBody Assignment assignmentInfo) throws BusinessException {
        Assignment assignment = assignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new BusinessException(String.format("Unknown assignment with id: %s", assignmentId)));
        ;

        assignment.setType(assignmentInfo.getType());
        assignment.setTitle(assignmentInfo.getTitle());
        assignmentRepository.save(assignment);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/assignments/quizzes", method = RequestMethod.GET)
    public ResponseEntity getAssignment(@RequestParam(value = "type", defaultValue = "") String quizType,
                                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "searchType", defaultValue = "") String searchType,
                                        @RequestParam(value = "searchContent", defaultValue = "") String searchContent) {

        Map data = null;
        if (quizType.equals("BASIC_QUIZ")) {
            data = quizCenterService.getBasicQuizzes(page, searchType, searchContent);
        } else if ("HOMEWORK_QUIZ".equals(quizType)) {
            data = quizCenterService.getHomeworkQuizzes(page, searchType, searchContent);
        }else if ("SIMPLE_CODING_QUIZ".equals(quizType)) {
            data = quizCenterService.getSimpleCodingQuizzes(page, searchType, searchContent);
        } else if ("ONLINE_CODING_QUIZ".equals(quizType)) {
            data = quizCenterService.getOnlineCodingQuizzes(page, searchType, searchContent);
        } else if ("ONLINE_LANGUAGE_QUIZ".equals(quizType)) {
            data = quizCenterService.getOnlineLanguageQuizzes(page, searchType, searchContent);
        } else if (("SUBJECTIVE_QUIZ").equals(quizType)) {
            data = quizCenterService.getSubjectiveQuizzes(page, searchType, searchContent);
        } else if ("LOGIC_QUIZ".equals(quizType)) {
            data = quizCenterService.getLogicQuizzes(page, searchType, searchContent);

        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/tasks/{taskId}/assignments/{assignmentId}/quizzes", method = RequestMethod.POST)
    public ResponseEntity addAssignmentQuiz(@PathVariable Long taskId,
                                            @PathVariable Long assignmentId,
                                            @RequestBody ArrayList<Long> quizIds,
                                            @Auth User current) throws BusinessException {
        if (quizIds.size() == 0) {
            throw new BusinessException("当前未选中题目");
        }
        List<AssignmentQuiz> assignmentQuizzes = assignmentQuizRepository.findByAssignmentId(assignmentId);
        if (assignmentQuizzes.size() != 0) {
            assignmentQuizRepository.deleteAllByAssignmentId(assignmentId);
        }

        HashMap data = new HashMap();
        quizIds.stream().map(quizId -> {
            AssignmentQuiz assignmentQuiz = new AssignmentQuiz();
            assignmentQuiz.setAssignmentId(assignmentId);
            assignmentQuiz.setQuizId(quizId);
            assignmentQuiz.setCreatorId(current.getId());
            AssignmentQuiz assignmentQuiz1 = assignmentQuizRepository.save(assignmentQuiz);
            data.put("uri", "/task/" + taskId + "/assignments/+" + assignmentId + "/quizzes/" + assignmentQuiz1.getId());
            return data;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(data, HttpStatus.NO_CONTENT);
    }

    private List getQuizzesByQuizIds(String ids, String type) {
        List quizzes = new ArrayList();
        if (ids.equals("")) {
            return quizzes;
        }
        if (type.equals("BASIC_QUIZ")) {
            quizzes = quizCenterService.getSelectingBasicQuizzes(ids);
        } else if ("HOMEWORK_QUIZ".equals(type)) {
            quizzes = quizCenterService.getSelectHomeworkQuizzes(ids);
        } else if ("ONLINE_CODING_QUIZ".equals(type)) {
            quizzes = quizCenterService.getSelectOnlineCodingQuizzes(ids);
        } else if ("ONLINE_LANGUAGE_QUIZ".equals(type)) {
            quizzes = quizCenterService.getSelectOnlineLanguageQuizzes(ids);
        } else if ("SUBJECTIVE_QUIZ".equals(type)) {
            quizzes = quizCenterService.getSelectSubjectiveQuizzes(ids);
        } else if ("LOGIC_QUIZ".equals(type)) {
            quizzes = quizCenterService.getSelectLogicQuizzes(ids);
        }
        return quizzes;
    }

    @RequestMapping(value = "/programs/{programId}/students/{studentId}/assignments", method = RequestMethod.GET)
    public ResponseEntity getStudentsTasks(@PathVariable Long programId, @PathVariable Long studentId) throws BusinessException {
        Program program = programRepository
                .findById(programId)
                .orElseThrow(() -> new BusinessException(String.format("Program not exist with id: %s", programId)));

        List<Topic> topics = topicRepository.findByProgramIdAndVisibleOrderByOrderNumberAsc(programId, true);
        List<Task> tasks = getAllTasks(topics);
        List<Task> currentTopicTasks;
        ArrayList data = new ArrayList();
        for (Topic topic : topics) {
            currentTopicTasks = tasks.stream()
                    .filter(task -> Objects.equals(task.getTopicId(), topic.getId()))
                    .collect(Collectors.toList());
            currentTopicTasks.sort(Comparator.comparing(Task::getOrderNumber));
            currentTopicTasks.stream().forEach(task -> getStatusForAssignment(task.getAssignments(), studentId, data));
        }

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    private void getStatusForAssignment(List<Assignment> assignments, Long studentId, ArrayList data) {
        assignments.forEach(assignment -> {
            HashMap assignmentInfo = new HashMap();
            List<ReviewQuiz> reviewQuizzes = reviewQuizRepository.findAllByAssignmentIdAndStudentId(assignment.getId(), studentId);

            ObjectMapper oMapper = new ObjectMapper();
            Map map = oMapper.convertValue(assignment, Map.class);
            Task task = taskRepository
                    .findById(assignment.getTaskId())
                    .orElse(new Task());

            Topic topic = topicRepository
                    .findById(task.getTopicId())
                    .orElse(null);

            map.put("status", assignmentService.getAssignmentStatus(assignment, reviewQuizzes));
            assignmentInfo.put("assignment", map);
            assignmentInfo.put("topic", topic);
            assignmentInfo.put("task", task);
            data.add(assignmentInfo);
        });
    }

    private List<Task> getAllTasks(List<Topic> topics) {
        List<Long> topicIds = topics.stream().map(Topic::getId).collect(Collectors.toList());
        return taskRepository.findByTopicIdIn(topicIds);
    }

//    数据迁移方法，把有多个quiz的assignment里面的quiz拉出来和assignment平级（迁移完毕可删除）

    @RequestMapping(value = "/dataMigration", method = RequestMethod.GET)
    @Transactional(rollbackFor = Exception.class)
    public void dataMigration() {
        List<Long> assignmentIds = assignmentQuizRepository.findDistinctAssignmentId();
        for (Long assignmentId : assignmentIds) {
            List<AssignmentQuiz> assignmentQuizs = assignmentQuizRepository.findAllByAssignmentId(assignmentId);
            Assignment assignment = assignmentRepository
                    .findById(assignmentId)
                    .orElse(null);
            if (!"BASIC_QUIZ".equals(assignment.getType())) {
                if (assignmentQuizs.size() > 1) {
                    for (int i = 1; i < assignmentQuizs.size(); i++) {
                        Assignment assignment1 = new Assignment(assignment.getTaskId(), assignment.getType(), assignment.getCreateTime(), assignment.getCreatorId(), assignment.getTitle());
                        assignmentRepository.save(assignment1);
                        Long oldAssignmentId = assignmentQuizs.get(i).getAssignmentId();
                        String type = assignment.getType();
                        Long quizId = assignmentQuizs.get(i).getQuizId();
                        Long newAssignmentId = assignment1.getId();


                        //更新reviewQuiz表中assignmentId
                        List<ReviewQuiz> reviewQuizs = reviewQuizRepository.findAllByAssignmentIdAndQuizId(oldAssignmentId, quizId);
                        if (reviewQuizs.size() != 0) {
                            for (ReviewQuiz reviewQuiz : reviewQuizs) {
                                reviewQuiz.setAssignmentId(newAssignmentId);
                                reviewQuizRepository.save(reviewQuiz);
                            }
                        }

                        //更新excellentQuizComment表中assignmentId
                        List<ExcellentQuizComment> excellentQuizComments = excellentQuizCommentRepository.findAllByAssignmentIdAndQuizId(oldAssignmentId, quizId);
                        if (excellentQuizComments.size() != 0) {
                            for (ExcellentQuizComment excellentQuizComment : excellentQuizComments) {
                                excellentQuizComment.setAssignmentId(newAssignmentId);
                                excellentQuizCommentRepository.save(excellentQuizComment);
                            }
                        }

                        // 更新quizSuggestion表中assignmentId
                        List<QuizSuggestion> quizSuggestions = quizSuggestionRepository.findAllByAssignmentIdAndQuizId(oldAssignmentId, quizId);
                        if (quizSuggestions.size() != 0) {
                            for (QuizSuggestion quizSuggestion : quizSuggestions) {
                                quizSuggestion.setAssignmentId(newAssignmentId);
                                quizSuggestionRepository.save(quizSuggestion);
                            }
                        }
                        quizCenterService.dataMigration(oldAssignmentId,quizId,newAssignmentId,type);

                        //更新assignmentQuiz表中assignmentId
                        assignmentQuizs.get(i).setAssignmentId(newAssignmentId);
                        assignmentQuizRepository.save(assignmentQuizs.get(i));
                    }
                }
            }
        }
    }
}