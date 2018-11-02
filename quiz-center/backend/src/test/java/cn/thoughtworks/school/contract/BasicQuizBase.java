package cn.thoughtworks.school.contract;

import cn.thoughtworks.school.Application;
import cn.thoughtworks.school.entities.*;
import cn.thoughtworks.school.repositories.BasicQuizRepository;
import cn.thoughtworks.school.repositories.BasicQuizSubmissionRepository;
import cn.thoughtworks.school.services.FlywayService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@AutoConfigureStubRunner
@Transactional
public class BasicQuizBase {
    @Autowired
    WebApplicationContext wac;
    @Autowired
    private BasicQuizRepository basicQuizRepository;
    @Autowired
    private BasicQuizSubmissionRepository basicQuizSubmissionRepository;

    @Before
    public void setup() throws Exception {
        initData();
        RestAssuredMockMvc.webAppContextSetup(wac);
    }

    private void initData() {
//        initSingleChoice();
//        initMultipleChoice();
//        initBlankQuiz();
        initBasicSubmission();
    }

    private void  initBasicSubmission() {
        BasicQuizSubmission basicQuizSubmission = new BasicQuizSubmission(4L, 1L, 26L, "1");
        basicQuizSubmissionRepository.save(basicQuizSubmission);
    }

    private void initSingleChoice() {
        String type = "SINGLE_CHOICE";
        String description = "单选题";
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("js", 25L, 1L));
        BasicQuiz basicQuiz = new BasicQuiz(description, type, "1");
        basicQuiz.setTags(tags);
        basicQuiz.setAvailable(true);
        basicQuiz.setMakerId(1L);
        basicQuiz = basicQuizRepository.save(basicQuiz);
        List<BasicQuizChoices> choices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            BasicQuizChoicesComplexPK complexPK = new BasicQuizChoicesComplexPK(i, basicQuiz.getId());
            BasicQuizChoices basicQuizChoice = new BasicQuizChoices(complexPK, i + "");

            choices.add(basicQuizChoice);
        }

        basicQuiz.setChoices(choices);

        basicQuizRepository.save(basicQuiz);
    }

    private void initMultipleChoice() {
        String type = "MULTIPLE_CHOICE";
        String description = "多选题";
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("java", 25L, 1L));
        BasicQuiz basicQuiz = new BasicQuiz(description, type, "1,4");
        basicQuiz.setTags(tags);
        basicQuiz.setAvailable(true);
        basicQuiz.setMakerId(1L);
        basicQuiz = basicQuizRepository.save(basicQuiz);
        List<BasicQuizChoices> choices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            BasicQuizChoicesComplexPK complexPK = new BasicQuizChoicesComplexPK(i, basicQuiz.getId());
            BasicQuizChoices basicQuizChoice = new BasicQuizChoices(complexPK, i + "");

            choices.add(basicQuizChoice);
        }

        basicQuiz.setChoices(choices);

        basicQuizRepository.save(basicQuiz);
    }

    private void initBlankQuiz() {
        String type = "BASIC_BLANK_QUIZ";
        String description = "填空题";
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("html", 25L, 1L));
        BasicQuiz basicQuiz = new BasicQuiz(description, type, "答案");
        basicQuiz.setTags(tags);
        basicQuiz.setAvailable(true);
        basicQuiz.setMakerId(1L);
        basicQuizRepository.save(basicQuiz);
    }
}
