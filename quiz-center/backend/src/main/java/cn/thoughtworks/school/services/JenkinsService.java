package cn.thoughtworks.school.services;

import cn.thoughtworks.school.entities.StackCommand;
import cn.thoughtworks.school.handlers.BusinessException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class JenkinsService {

    @Value("${add-stack-jenkins-url}")
    private String addStackJenkinsUrl;

    @Value("${jenkins.host}")
    private String jenkinsHost;

    @Value("${github.token}")
    private String githubToken;

    @Value("${github.cert}")
    private String githubCert;

    @Autowired
    StackCommandService stackCommandService;

    @HystrixCommand(fallbackMethod = "fallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "1000")})
    public String addStack(String image, String callbackUrl) {

        RestTemplate template = new RestTemplate();

        MultiValueMap jenkinsData = new LinkedMultiValueMap();
        jenkinsData.add("github_token", githubToken);
        jenkinsData.add("image", image);
        jenkinsData.add("callback_url", callbackUrl);
        System.out.println(jenkinsData.toString());
        System.out.println(callbackUrl);
        System.out.println(addStackJenkinsUrl + "addStackJenkinsUrl");

        String url = addStackJenkinsUrl + "/buildWithParameters";
        return template.postForObject(url, jenkinsData, String.class);
    }

    @HystrixCommand(fallbackMethod = "fallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "1000")})
    public Map triggerSingleStackProgrammingQuizJob(String gitUrl, String callbackUrl) {
        String url = getAddQuizUrl();
        RestTemplate template = new RestTemplate();

        MultiValueMap requestData = new LinkedMultiValueMap();

        requestData.add("git_url", gitUrl);
        requestData.add("github_token", githubToken);
        requestData.add("github_cert", githubCert);
        requestData.add("callback_url", callbackUrl);

        ResponseEntity<String> response = template.postForEntity(url, requestData, String.class);

        Map result = new HashMap();
        result.put("status", response.getStatusCodeValue());

        return result;
    }

    @SuppressWarnings("unused")
    private String fallback(String goodsId) {
        return "无法访问";
    }

    @SuppressWarnings("unused")
    private Map fallback(String image, String callbackUrl) {
        Map result = new HashMap();
        result.put("status", 400);
        result.put("message", String.format("Can't access %s", getAddQuizUrl()));

        return result;
    }

    private String getAddQuizUrl() {
        return jenkinsHost + "/job/ADD-SINGLE-STACK-PROGRAMMING-QUIZ/buildWithParameters";
    }


    // HystrixCommand
    public HttpStatus triggerSingleStackProgrammingQuizSubmissionJob(String userAnswerRepo
            , String branch, String callbackUrl, String image, String answerPath)
            throws RestClientException {

        String url = String.format("%s/job/ADD-SINGLE-STACK-PROGRAMMING-QUIZ-SUBMISSION/buildWithParameters", jenkinsHost);
//        String url = String.format("%s/job/TEST2/buildWithParameters", jenkinsHost);
        RestTemplate template = new RestTemplate();
        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
        // 回调地址
        requestData.add("callback_url", callbackUrl);
        // 作业git地址
        requestData.add("user_answer_repo", userAnswerRepo);
        // 作业分支
        requestData.add("branch", branch);
        // docker镜像地址
        requestData.add("image", image);
        // answer地址
        requestData.add("answer_path", answerPath);
        ResponseEntity<String> response = template.postForEntity(url, requestData, String.class);
        return response.getStatusCode();
    }

    public HttpStatus triggerSingleLanguageCodingSubmissionJob(String testData
            , String userAnswerCode, String callbackUrl, String image, String language)
            throws RestClientException, BusinessException {
//        String url = String.format("%s/job/TEST_ADDONLINESUBMISSION/buildWithParameters", jenkinsHost);
        String url = String.format("%s/job/ADD-SINGLE-LANGUAGE-ONLINE-CODING-SUBMISSION/buildWithParameters", jenkinsHost);
        StackCommand stackCommand = stackCommandService.getStackCommandByName(language);
        if (stackCommand == null) {
            throw new BusinessException(String.format("can not find %s", language));
        }
        RestTemplate template = new RestTemplate();
        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
        // 测试用例
        requestData.add("test_data", testData);
        // 用户在线代码
        requestData.add("user_answer_code", userAnswerCode);
        // 回调地址
        requestData.add("callback_url", callbackUrl);
        // 镜像
        requestData.add("image", image);
        // 语言
        requestData.add("language", language);
        // 编译命令
        requestData.add("compile", stackCommand.getCompile());
        // 运行命令
        requestData.add("execute", stackCommand.getExecute());
        // 语言文件后缀
        requestData.add("sourcePostfix", stackCommand.getSourcePostfix());
        // 运行文件后缀
        requestData.add("executePostfix", stackCommand.getExecutePostfix());
        ResponseEntity<String> response = template.postForEntity(url, requestData, String.class);
        return response.getStatusCode();
    }


}
