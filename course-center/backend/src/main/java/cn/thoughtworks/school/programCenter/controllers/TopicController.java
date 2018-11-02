package cn.thoughtworks.school.programCenter.controllers;

import cn.thoughtworks.school.programCenter.entities.Task;
import cn.thoughtworks.school.programCenter.entities.Topic;
import cn.thoughtworks.school.programCenter.exceptions.BusinessException;
import cn.thoughtworks.school.programCenter.repositories.TaskRepository;
import cn.thoughtworks.school.programCenter.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(value = "/api")
public class TopicController {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private TaskRepository taskRepository;
            
    @RequestMapping(value = "/programs/{programId}/topicTask", method = RequestMethod.GET)
    public ResponseEntity getAllTopicsTasks(@PathVariable Long programId) throws Exception {
        List<Topic> topics = topicRepository.findByProgramIdOrderByOrderNumberAsc(programId);
        if(topics.size()==0){
            return new ResponseEntity<>(topics,HttpStatus.OK);
        }
        ArrayList<HashMap> topicTasksArray = new ArrayList();

        for(Topic topic:topics){
            HashMap<String, Object> topicTasks = new HashMap();
            List<Task> tasks = taskRepository.findTaskByTopicIdOrderByOrderNumberAsc(topic.getId());
            topicTasks.put("topics",topic);
            topicTasks.put("tasks",tasks);
            topicTasksArray.add(topicTasks);
        }
        return new ResponseEntity<>(topicTasksArray, HttpStatus.OK);
    }

    @RequestMapping(value = "/topics", method = RequestMethod.POST)
    public ResponseEntity addTopic(@RequestBody Topic topic) {
        Topic newTopic = topicRepository.save(topic);
        newTopic.setOrderNumber(newTopic.getId());
        topicRepository.save(newTopic);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/topics/{topicId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTopic(@PathVariable Long topicId) throws Exception {

        if (!topicRepository.existsById(topicId)) {
            throw new BusinessException("删除的Topic 不存在！");
        }
        List<Task> tasks = taskRepository.findTaskByTopicIdOrderByOrderNumberAsc(topicId);
        if (tasks != null) {
            for (Task task : tasks) {
                taskRepository.deleteById(task.getId());
            }
        }
        topicRepository.deleteById(topicId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/topics/{topicId}", method = RequestMethod.PUT)
    public ResponseEntity updateTopic(@PathVariable Long topicId, @RequestBody Map<String, String> data) throws Exception {
        Topic topic = topicRepository
                .findById(topicId)
                .orElseThrow(()-> new BusinessException("更新的Topic 不存在"));

        String title = data.get("title");
        topic.setTitle(title);
        topicRepository.save(topic);
        return new ResponseEntity<>(topic,HttpStatus.NO_CONTENT);
    }

    @PutMapping("/topics")
    public ResponseEntity dragSort(@RequestBody List<Topic> topics) {
        for(int i = 0; i < topics.size();++i) {
            topics.get(i).setOrderNumber((long) i);
        }

        topicRepository.saveAll(topics);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/topics/{topicId}/visibility", method = RequestMethod.PUT)
    public ResponseEntity changeTopicVisible(@PathVariable Long topicId, @RequestBody Map data) throws Exception {
        Topic topic = topicRepository
                .findById(topicId)
                .orElseThrow(()-> new BusinessException("更新的Topic 不存在"));

        if (Objects.equals(data.get("visibility"),false)) {
            taskRepository.updateVisibilityByTopicId(topic.getId(),false);
        }
        topic.setVisible((Boolean) data.get("visibility"));
        topicRepository.save(topic);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
