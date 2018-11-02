package cn.thoughtworks.school.contract;

import cn.thoughtworks.school.Application;
import cn.thoughtworks.school.entities.SubjectiveQuiz;
import cn.thoughtworks.school.repositories.SubjectiveQuizRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@Transactional
public class SubjectiveQuizBase {

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private SubjectiveQuizRepository subjectiveQuizRepository;

    @Before
    public void setup() throws Exception {
        RestAssuredMockMvc.webAppContextSetup(wac);
    }
}
