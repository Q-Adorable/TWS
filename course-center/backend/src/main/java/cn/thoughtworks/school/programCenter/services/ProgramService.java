package cn.thoughtworks.school.programCenter.services;

import cn.thoughtworks.school.programCenter.entities.*;
import cn.thoughtworks.school.programCenter.exceptions.BusinessException;
import cn.thoughtworks.school.programCenter.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProgramService {


    @Autowired
    private ReviewQuizRepository reviewQuizRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserProgramRepository userProgramRepository;
    @Autowired
    private UserCenterService userCenterService;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private AssignmentQuizRepository assignmentQuizRepository;

    @Cacheable(value = "LEADER_BOARD_CACHE")
    public List getStudentLeaderBoard() {
        return cacheLeaderBoard();
    }

    public List cacheLeaderBoard() {
        List result = new ArrayList();
        List<Program> programs = programRepository.findAll();
        programs.forEach(program -> {
            Map temp = new HashMap();
            List leaderBoards = new ArrayList();
            List<Task> tasks = taskRepository.findByProgramId(program.getId());
            List<Long> ids = getTaskIds(tasks);
            List<UserProgram> userPrograms = userProgramRepository.findByProgramId(program.getId());

            userPrograms.forEach(item -> {
                log.info(String.format("userId:"+item.getUserId()+",programId:"+item.getProgramId()));
                ResponseEntity userInfoResponse = userCenterService.getUserInfo(item.getUserId());
                if (Objects.nonNull(userInfoResponse)) {
                    Map userInfo = (Map) userInfoResponse.getBody();
                    String name = getStudentName(userInfo);
                    Long studentId = getStudentId(userInfo);
                    leaderBoards.add(getUserBoard(ids, studentId, name));
                }
            });

            leaderBoards.sort((Comparator<Map>) (o1, o2) ->
                    (int) (Double.valueOf(o2.get("finishHomeworkPoint").toString()) - Double.valueOf(o1.get("finishHomeworkPoint").toString())));
            temp.put("currentProgramLeaderBoard", leaderBoards);
            temp.put("programId", program.getId());
            result.add(temp);
        });
        return result;
    }


    private Long getStudentId(Map userInfo) {
        if (Objects.isNull(userInfo.get("id"))) {
            return Long.valueOf(userInfo.get("userId").toString());
        } else {
            return Long.valueOf(userInfo.get("id").toString());
        }
    }

    private String getStudentName(Map userInfo) {
        if (Objects.isNull(userInfo.get("name"))) {
            return userInfo.get("userName").toString();
        } else {
            return userInfo.get("name").toString();
        }
    }

    public Map getUserBoard(List<Long> taskIds, Long studentId, String name) {
        Map result = new HashMap();

        result.put("name", name);
        result.put("studentId", studentId);
        Long finishSectionCount = reviewQuizRepository.getFinishSectionCount(studentId, taskIds);
        result.put("finishHomeworkCount", Objects.isNull(finishSectionCount) ? 0 : finishSectionCount);
        Long scores = reviewQuizRepository.getScores(studentId, taskIds);
        result.put("finishHomeworkPoint", Objects.isNull(scores) ? 0 : scores);

        return result;
    }

    private List<Long> getTaskIds(List<Task> tasks) {
        List<Long> result = new ArrayList<>();
        for (Task task : tasks) {
            result.add(task.getId());
        }

        return result;
    }


    public void copyProgram(Long programId, User current) throws BusinessException {
        Program program = programRepository.findById(programId).orElseThrow(() -> new BusinessException("训练营不存在"));
        Program duplicationProgram = copyProgramProcess(program, current);
        List<Topic> topics = topicRepository.findByProgramId(program.getId());
        List<Task> tasks = taskRepository.findByProgramId(programId);

        for (Topic topic : topics) {
            List<Task> currentTopicTasks = tasks.stream().filter(task -> task.getTopicId().equals(topic.getId())).collect(Collectors.toList());
            Topic duplicationTopic = copyTopicsProcess(topic,duplicationProgram);
            copyTasksProcess(duplicationProgram, duplicationTopic,currentTopicTasks);
        }
    }

    private void copyTasksProcess(Program program, Topic topic, List<Task> tasks) {
        for (Task task: tasks) {
            Task duplicationTask = task.copy(program, topic,task);
            taskRepository.save(duplicationTask);
            copyAssignmentsProcess(task, duplicationTask);
        }
    }

    private void copyAssignmentsProcess(Task task, Task duplicationTask) {
        List<Assignment> assignments = task.getAssignments();
        for (Assignment assignment : assignments) {
            Assignment duplicationAssignment= assignment.copyAssignment(assignment,duplicationTask);
            assignmentRepository.save(duplicationAssignment);
        }
    }

    private Topic copyTopicsProcess(Topic topic,Program program) {
        Topic duplicationTopic = topic.copy(topic,program);
        return topicRepository.save(duplicationTopic);
    }

    private Program copyProgramProcess(Program program, User current) {
        Program duplicationProgram = program.copy(program,current.getId());
        return programRepository.save(duplicationProgram);
    }

    public void copyTaskToTopic(List<Long> sourceTaskIds, Long topicId) throws BusinessException {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new BusinessException("topic 不存在"));
        Program program = programRepository.findById(topic.getProgramId()).orElseThrow(() -> new BusinessException("program 不存在"));
        List<Task> tasks = taskRepository.findAllById(sourceTaskIds);
        copyTasksProcess(program, topic, tasks);
    }

    public void moveTaskToTopic(List<Long> sourceTaskIds, Long topicId) throws BusinessException {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new BusinessException("topic 不存在"));
        Program program = programRepository.findById(topic.getProgramId()).orElseThrow(() -> new BusinessException("program 不存在"));
        taskRepository.moveTasksToTopic(sourceTaskIds, program.getId(), topicId);
    }

    public List searchStudentsPrograms(List<Map> students) {
        List result =  students.stream().map(student->{
            List<Program> programs = programRepository.getProgramsByUserProgramStudentId(Long.parseLong(student.get("id").toString()));
            student.put("programs", programs);
            return student;
        }).collect(Collectors.toList());
        return result;
    }

    public List searchTutorsPrograms(List<Map> tutors) {
        return tutors.stream().map(tutor->{
            List<Program> programs = programRepository.getProgramsByUserProgramTutorId(Long.parseLong(tutor.get("id").toString()));
            tutor.put("programs", programs);
            return tutor;
        }).collect(Collectors.toList());
    }

    public void updateRegisterProgramLink(Map data) throws BusinessException {
        String type = data.get("type").toString();
        Long programId = Long.parseLong(data.get("programId").toString());
        boolean enable = (boolean) data.get("enable");
        Program program = programRepository.findById(programId).orElseThrow(() ->new BusinessException("训练营不存在"));
        if ("tutor".equals(type)) {
            program.setTutorLink(enable);
        }
        if ("student".equals(type)) {
            program.setStudentLink(enable);
        }
        programRepository.save(program);
    }
}
