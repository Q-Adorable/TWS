package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotations.Auth;
import cn.thoughtworks.school.entities.Stack;
import cn.thoughtworks.school.entities.User;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.StackRepository;
import cn.thoughtworks.school.services.Utils;
import cn.thoughtworks.school.services.JenkinsService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping(value = "/api/v3/stacks")
public class StackController {

    private final Logger logger = LoggerFactory.getLogger(StackController.class);

    @Autowired
    private StackRepository stackRepository;
    @Autowired
    private Utils utils;
    @Value("${add-stack-jenkins-url}")
    private String addStackJenkinsUrl;

    @Value("${delete-stack-jenkins-url}")
    private String deleteStackJenkinsUrl;

    @Value("${quiz-center-url}")
    private String quizCenterUrl;
    @Value("${jenkins-callback-status.success}")
    private Integer successStatus;
    @Value("${jenkins-callback-status.progress}")
    private Integer progressStatus;
    @Value("${jenkins-callback-status.line-up}")
    private Integer lineUpStatus;

    @Value("${jenkins-callback-status.failure}")
    private Integer failureStatus;

    private String githubToken = "ca56d5b5467bc58ed784d14483bc233e87291fd0";

    @Autowired
    private JenkinsService jenkinsService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity findAll() {

        List<Stack> stackList = stackRepository.findAll();
        return new ResponseEntity<>(stackList, HttpStatus.OK);
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public ResponseEntity findAllTags(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(page - 1, size, sort);
        Page stacksPage = stackRepository.findAll(pageable);
        Page results = utils.format(stacksPage, pageable);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity addStack(@RequestBody Map data, @Auth User current) throws BusinessException {
        String image = (String) data.get("image");
        String[] images = image.split(":");
        RestTemplate template = new RestTemplate();
        String url = "https://registry.hub.docker.com/v1/repositories/" + images[0] + "/tags";

        ResponseEntity<List> result = template.getForEntity(url, List.class);
        List<Map> tags = result.getBody();
        Map currentTag = tags.stream().filter(tag -> tag.get("name").equals(images[1])).findFirst().orElse(null);
        if (currentTag == null) {
            throw new BusinessException("未找到当前镜像的该版本");
        }
        if (stackRepository.findStackByImage(image) != null) {
            throw new BusinessException("该镜像已存在");
        }

        Stack stack = new Stack();
        stack.setTitle(image);
        stack.setImage(image);
        stack.setDescription((String) data.get("description"));
        stack.setMakerId(current.getId());
        stack.setReferenceNumber(0L);
        stack.setAvailable(false);
        Stack newStack = stackRepository.save(stack);

        Long newStackId = newStack.getId();

        String callbackUrl = quizCenterUrl + "/api/v3/stacks/" + newStackId + "/status";
        String state = jenkinsService.addStack(image, callbackUrl);

        Map<String, Object> body = new HashMap<>();
        body.put("uri", "/api/v3/stacks/" + newStackId);
        body.put("id", newStackId);
        body.put("state", state);

        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{stackId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteStack(@PathVariable Long stackId) throws BusinessException {
        // todo 往下合并为一个查询
        Stack stack = stackRepository
                .findById(stackId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown stack with id: %s", stackId)
                ));
        ;

        if (stack.getReferenceNumber() != 0) {
            throw new BusinessException(
                    String.format("Unknown stack with id: %s", stackId)
            );
        }
        stack.setAvailable(false);
        stackRepository.save(stack);
        RestTemplate template = new RestTemplate();
        String callbackUrl = quizCenterUrl + "/api/v3/stacks/deleteStatus";
        MultiValueMap jenkinsData = new LinkedMultiValueMap();
        jenkinsData.add("github_token", githubToken);
        jenkinsData.add("image", stack.getImage());
        jenkinsData.add("callback_url", callbackUrl);

        template.postForObject(deleteStackJenkinsUrl, jenkinsData, String.class);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{stackId}/status", method = RequestMethod.PUT)
    public ResponseEntity updateStackStatus(@RequestBody Map body, @PathVariable Long stackId) throws BusinessException {
        Stack theStack = stackRepository
                .findById(stackId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown stack with id: %s", stackId)
                ));

        Integer buildNumber = (Integer) body.get("buildNumber");
        String status = (String) body.get("status");

        logger.info(String.format("stackId : %s", stackId));
        logger.info(String.format("RequestBody : %s", new JSONObject(body)));

        if (buildNumber != null && theStack.getBuildNumber() == null) {
            theStack.setBuildNumber(buildNumber.longValue());
        }

        // todo 修改为枚举，并且要和jenkins相互配合
        if (status != null && status.equals("SUCCESS")) {
            theStack.setAvailable(true);
        }

        stackRepository.save(theStack);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @RequestMapping(value = "/{stackId}/status", method = RequestMethod.GET)
    public ResponseEntity searchStackStatus(@PathVariable Long stackId) throws BusinessException {

        Stack stack = stackRepository
                .findById(stackId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown stack with id: %s", stackId)
                ));

        HashMap result = new HashMap();
        if (stack.getAvailable()) {
            result.put("status", successStatus);
            result.put("id", stackId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        if (stack.getBuildNumber() == null) {
            result.put("status", lineUpStatus);
            result.put("id", stackId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        String getLogs = addStackJenkinsUrl + "/" + stack.getBuildNumber() + "/consoleText";
        System.out.println(getLogs + "getLogs  vvvvvvv");
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> data = template.getForEntity(getLogs, String.class);
        result.put("logs", data.getBody());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteStatus", method = RequestMethod.PUT)
    public ResponseEntity jenkinsDeleteImageCallBack(@RequestParam(value = "status", required = false) String status, @RequestParam(value = "image", required = false) String image) throws BusinessException {
        Stack currentStack = stackRepository.findStackByImage(image);
        if (Integer.parseInt(status) != successStatus) {
            currentStack.setAvailable(true);
            stackRepository.save(currentStack);
            throw new BusinessException("当前镜像正在使用,不能删除");
        }

        if (currentStack == null) {
            throw new BusinessException("后台错误");
        }
        stackRepository.deleteById(currentStack.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @RequestMapping(value = "/{stackId}", method = RequestMethod.GET)
    public ResponseEntity judgeStackIsDelete(@PathVariable Long stackId) throws BusinessException {
        Stack stack = stackRepository
                .findById(stackId)
                .orElse(null);

        HashMap result = new HashMap();
        if (stack == null) {
            result.put("status", successStatus);
        } else if (stack.getAvailable()) {
            result.put("status", failureStatus);
        } else {
            result.put("status", progressStatus);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
