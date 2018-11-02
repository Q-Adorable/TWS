package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.entities.Task;
import cn.thoughtworks.school.programCenter.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping(value = "/api/SQL")
public class GenerateSQL {
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("programs/{programId}")
    public ResponseEntity generateProgramSQL(@PathVariable Long programId) {
        StringBuffer result = new StringBuffer("select user.userId id, user.name 姓名, \n");
        result.append("((select count(1) from reviewQuiz where studentId = user.userId and assignmentId in \n")
                .append("    (select id from assignment where taskId in ((select id from task where programId=55))) \n")
                .append("and (status = \"已完成\" or status = \"优秀\")) / \n")
                .append("(select count(1) from assignment where taskId in (select id from task where programId=55))) AS '作业完成率',\n");

        List<String> taskTitles = taskRepository.findTaskByProgramIdAndVisible(programId, true)
                .stream().map(Task::getTitle).collect(Collectors.toList());
        taskTitles.forEach(title ->
                result.append("    MAX(\n")
                        .append("        CASE   task.title")
                        .append("        WHEN  '").append(title).append("'\n")
                        .append("       THEN  (case\n")
                        .append("                when\n" +
                                "                    (select count(1) from reviewQuiz  where assignmentId in (select id from assignment where taskId=task.id) and studentId=user.userId and (status = \"已完成\" or status = \"优秀\") )\n" +
                                "                    =\n" +
                                "                    (select count(1) from assignment  where taskId=task.id ) THEN \"已完成\"\n" +
                                "                when\n" +
                                "                    (select count(1) from reviewQuiz  where assignmentId in (select id from assignment where taskId=task.id) and studentId=user.userId and (status=\"已提交\"))\n" +
                                "                    =\n" +
                                "                    (select count(1) from assignment  where taskId=task.id ) THEN \"待评阅\"\n" +
                                "                 ELSE  \"未完成\"\n" +
                                "                 END\n" +
                                "                )\n" +
                                "        ELSE 0 END\n")
                        .append("        ) AS '").append(title).append("',")
        );
        result.deleteCharAt(result.length() - 1);
        result.append("from UserCenter.userDetail user\n")
                .append("left join (select * from task ) task on task.programId ='").append(programId).append("'\n")
                .append("left join (select * from userProgram) userProgram on userProgram.programId ='").append(programId).append("'\n")
                .append("where userProgram.userId=user.userId\n" +
                "group by user.name");
        return new ResponseEntity(result.toString(), HttpStatus.OK);
    }

}
