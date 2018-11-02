package cn.thoughtworks.school.contract;

import cn.thoughtworks.school.Application;
import cn.thoughtworks.school.entities.Stack;
import cn.thoughtworks.school.repositories.StackRepository;
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
//@AutoConfigureStubRunner(ids = "cn.thoughtworks.school:userCenterBackend:+:stubs:10001", repositoryRoot = "http://ec2-54-222-235-15.cn-north-1.compute.amazonaws.com.cn:8081/repository/maven-snapshots/")
@ActiveProfiles("test")
@Transactional
public class StackBase {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private StackRepository stackRepository;

    @Before
    public void setup() throws Exception {
        RestAssuredMockMvc.webAppContextSetup(wac);
    }
}
