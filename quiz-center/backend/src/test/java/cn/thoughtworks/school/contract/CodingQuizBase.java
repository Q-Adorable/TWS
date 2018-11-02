package cn.thoughtworks.school.contract;

import cn.thoughtworks.school.Application;
import cn.thoughtworks.school.entities.HomeworkQuiz;
import cn.thoughtworks.school.entities.Stack;
import cn.thoughtworks.school.entities.Tag;
import cn.thoughtworks.school.repositories.HomeworkQuizRepository;
import cn.thoughtworks.school.repositories.StackRepository;
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

import java.util.HashSet;
import java.util.Set;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@AutoConfigureStubRunner
@Transactional
public class CodingQuizBase {
    @Autowired
    WebApplicationContext wac;

    @Before
    public void setup() throws Exception {
        RestAssuredMockMvc.webAppContextSetup(wac);
    }
}
