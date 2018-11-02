package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.annotations.Auth;
import cn.thoughtworks.school.programCenter.entities.*;
import cn.thoughtworks.school.programCenter.exceptions.BusinessException;
import cn.thoughtworks.school.programCenter.repositories.*;
import cn.thoughtworks.school.programCenter.services.UserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api")
public class MyStudentController {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ReviewQuizRepository reviewQuizRepository;
    @Autowired
    private UserCenterService userCenterService;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserProgramRepository userProgramRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private AssignmentQuizRepository assignmentQuizRepository;

    @PostMapping("/myStudents/programs/{programId}/followers/{followerId}")
    public ResponseEntity follow(@PathVariable Long programId, @PathVariable Long followerId, @Auth User current) throws BusinessException {
        Follow follow = followRepository.findByProgramIdAndTutorIdAndStudentId(programId, current.getId(), followerId);
        if (Objects.nonNull(follow)) {
            throw new BusinessException("已关注该学员");
        }
        follow = new Follow();
        follow.setProgramId(programId);
        follow.setStudentId(followerId);
        follow.setTutorId(current.getId());
        followRepository.save(follow);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/programs/{programId}/tutors/{tutorId}/students/{studentIds}")
    public ResponseEntity followStudents(@PathVariable Long programId, @PathVariable Long tutorId, @PathVariable String studentIds) throws BusinessException {
        List<String> needToFollowed = Arrays.asList(studentIds.split(","));
        List<Long> alreadyFollowIds = followRepository.findByProgramIdAndTutorId(programId, tutorId)
                .stream().map(Follow::getId).collect(Collectors.toList());
        needToFollowed.stream().forEach(studentId -> {
                    if (!alreadyFollowIds.contains(Long.valueOf(studentId))) {
                        Follow follow = Follow.builder()
                                .programId(programId)
                                .tutorId(tutorId)
                                .studentId(Long.valueOf(studentId))
                                .build();
                        followRepository.save(follow);
                    }
                }
        );

        return new ResponseEntity(needToFollowed, HttpStatus.CREATED);
    }

    @DeleteMapping("/programs/{programId}/tutors/{tutorId}/students/{studentIds}")
    public ResponseEntity unFollowStudents(@PathVariable Long programId, @PathVariable Long tutorId, @PathVariable String studentIds) throws BusinessException {
        List<Long> needToUnFollowed = Arrays.stream(studentIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        followRepository.unFollowByProgramIdAndTutorIdAndStudentIds(programId, tutorId, needToUnFollowed);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/v2/students/{studentId}")
    public ResponseEntity getStudent(@PathVariable Long studentId) {
        return userCenterService.getUserInfo(studentId);
    }

    @DeleteMapping("/myStudents/programs/{programId}/followers/{followerId}")
    public ResponseEntity unFollow(@PathVariable Long programId, @PathVariable Long followerId, @Auth User current) throws BusinessException {
        Follow follow = followRepository.findByProgramIdAndTutorIdAndStudentId(programId, current.getId(), followerId);
        if (Objects.isNull(follow)) {
            throw new BusinessException("未关注该学员");
        }
        followRepository.delete(follow);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/myStudents/programs/{programId}", method = RequestMethod.GET)
    public ResponseEntity getMyStudents(@Auth User current, @PathVariable Long programId) {
        ArrayList result = new ArrayList();
        List<Follow> myFollowStudentIds = followRepository.findByProgramIdAndTutorId(programId, current.getId());
        List<Map> currentProgramFollowStudents = getCurrentProgramFollowStudents(myFollowStudentIds);

        for (Map myStudent : currentProgramFollowStudents) {
            HashMap studentGrade = new HashMap();
            Integer sumGrade = getAllTaskGrade(programId, ((Integer) myStudent.get("id")).longValue());
            studentGrade.put("student", myStudent);
            studentGrade.put("sumPoints", sumGrade);
            result.add(studentGrade);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private List getCurrentProgramFollowStudents(List<Follow> myFollowStudentIds) {
        String ids = myFollowStudentIds.stream().map(follow -> follow.getStudentId().toString()).collect(Collectors.joining(","));
        return userCenterService.getUsersByIds(ids);
    }

    private Integer getAllTaskGrade(Long programId, Long studentId) {
        List<Task> tasks = taskRepository.findTaskByProgramIdAndVisible(programId, true);
        Long scores = reviewQuizRepository.getScores(studentId, getTaskIds(tasks));
        return Objects.isNull(scores) ? 0 : Integer.parseInt(scores.toString());
    }

    private List<Long> getTaskIds(List<Task> tasks) {
        List<Long> result = new ArrayList<>();
        for (Task task : tasks) {
            result.add(task.getId());
        }
        return result;
    }


    @GetMapping("/myStudents/programs/{programId}/{query}")
    public ResponseEntity getUserByNameOrEmail(@PathVariable String query, @PathVariable Long programId, @Auth User current) {
        Map result = new HashMap();
        List<Map> searchStudents =  userCenterService.getUserByNameOrEmail(query).getBody();
        List<UserProgram> programStudents = userProgramRepository.findByProgramId(programId);
        searchStudents = searchStudents.stream().filter(student -> {
                    Optional first = programStudents.stream().filter(programStudent ->
                            Objects.equals(Long.valueOf(student.get("id").toString()), programStudent.getUserId())
                    ).findFirst();
                    return first.isPresent();
                }
        ).collect(Collectors.toList());
        result.put("searchUsers", searchStudents);
        result.put("followers", followRepository.findByProgramIdAndTutorId(programId, current.getId()));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/v2/myStudents/programs/{programId}/students/assignments")
    public ResponseEntity getMyStudentsSectionsStatus(
            @PathVariable Long programId, @Auth User current) {
        List result = new ArrayList();
        List<Topic> topics = topicRepository.findByProgramIdAndVisibleOrderByOrderNumberAsc(programId, true);
        List<Follow> myFollowStudentIds = followRepository.findByProgramIdAndTutorId(programId, current.getId());
        topics.forEach(topic -> {
            List<Task> tasks = taskRepository.findByTopicIdAndVisibleOrderByOrderNumberAsc(topic.getId(), true);
            tasks.forEach(task -> task.getAssignments().forEach(assignment -> {
                Map<String, Object> taskAssignmentsStatus = getAssignmentStatus(assignment, myFollowStudentIds);
                Map map = new HashMap();
                map.put("topic", topic);
                map.put("task", task);
                map.put("assignment", assignment);
                map.put("situation", taskAssignmentsStatus);
                result.add(map);
            }));
        });
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private Map<String, Object> getAssignmentStatus(Assignment assignment, List<Follow> myFollows) {
        Map<String, Object> assignmentInfo = new HashMap();
        List<Long> studentIds = myFollows.stream().map(Follow::getStudentId).collect(Collectors.toList());
        List<ReviewQuiz> reviewQuizzes = reviewQuizRepository.findAllByAssignmentIdAndStudentIdIn(assignment.getId(), studentIds);
        int unfinishedCount = studentIds.size() - reviewQuizzes.size();
        int reviewingCount = Integer.parseInt(reviewQuizzes.stream().filter(item -> Objects.equals("已提交", item.getStatus())).count() + "");
        int finishedCount = Integer.parseInt(reviewQuizzes.stream().filter(item -> Objects.equals("已完成", item.getStatus())).count() + "");
        int excellentCount = Integer.parseInt(reviewQuizzes.stream().filter(item -> Objects.equals("优秀", item.getStatus())).count() + "");
        assignmentInfo.put("excellentCount", excellentCount);
        assignmentInfo.put("finishedCount", finishedCount);
        assignmentInfo.put("reviewingCount", reviewingCount);
        assignmentInfo.put("unfinishedCount", unfinishedCount);
        return assignmentInfo;
    }

    @GetMapping("/v2/myStudents/programs/{programId}/assignments/{assignmentId}/students")
    public ResponseEntity getStudentAssignmentStatus(@PathVariable Long programId, @PathVariable Long assignmentId, @Auth User current) throws BusinessException {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() ->
                new BusinessException("当前作业不存在")
        );
        List<Follow> myFollowStudentIds = followRepository.findByProgramIdAndTutorId(programId, current.getId());
        List<Map> myStudents = getCurrentProgramFollowStudents(myFollowStudentIds);
        Map<String, Object> studentsInfo = new HashMap();
        List<Map> excellentStudents = new ArrayList<>();
        List<Map> finishedStudents = new ArrayList<>();
        List<Map> reviewingStudents = new ArrayList<>();
        List<Map> unfinishedStudents = new ArrayList<>();

        myStudents.forEach(student -> {
            List<AssignmentQuiz> assignmentQuizzes = assignmentQuizRepository.findByAssignmentId(assignment.getId());
            List<ReviewQuiz> reviewQuizzes = reviewQuizRepository.findAllByAssignmentIdAndStudentId(assignment.getId(), Long.valueOf(student.get("id").toString()));
            if ("BASIC_QUIZ".equals(assignment.getType())) {
                dealWithBasicQuiz2(excellentStudents, finishedStudents, unfinishedStudents, student, reviewQuizzes);
            } else if (isUnfinished(assignmentQuizzes, reviewQuizzes)) {
                unfinishedStudents.add(student);
            } else {
                List finished = reviewQuizzes.stream().filter(reviewQuiz -> reviewQuiz.getStatus().equals("已完成")).collect(Collectors.toList());
                List excellent = reviewQuizzes.stream().filter(reviewQuiz -> reviewQuiz.getStatus().equals("优秀")).collect(Collectors.toList());
                if (finished.size() == reviewQuizzes.size()) {
                    finishedStudents.add(student);
                } else if (excellent.size() == reviewQuizzes.size()) {
                    excellentStudents.add(student);
                } else if (finished.size() + excellent.size() == reviewQuizzes.size()) {
                    finishedStudents.add(student);
                } else {
                    reviewingStudents.add(student);
                }
            }
        });
        studentsInfo.put("excellentStudents", excellentStudents);
        studentsInfo.put("finishedStudents", finishedStudents);
        studentsInfo.put("reviewingStudents", reviewingStudents);
        studentsInfo.put("unfinishedStudents", unfinishedStudents);
        return new ResponseEntity<>(studentsInfo, HttpStatus.OK);
    }

    private void dealWithBasicQuiz2(List<Map> excellentStudents, List<Map> finishedStudents, List<Map> unfinishedStudents, Map student, List<ReviewQuiz> reviewQuizzes) {
        if (reviewQuizzes.size() == 0) {
            unfinishedStudents.add(student);
        } else {
            ReviewQuiz reviewQuiz = reviewQuizzes.get(0);
            if ("已完成".equals(reviewQuiz.getStatus())) {
                finishedStudents.add(student);
            } else {
                excellentStudents.add(student);
            }
        }
    }

    private boolean isUnfinished(List<AssignmentQuiz> assignmentQuizzes, List<ReviewQuiz> reviewQuizzes) {
        return !(assignmentQuizzes.size() == reviewQuizzes.size());
    }
}
