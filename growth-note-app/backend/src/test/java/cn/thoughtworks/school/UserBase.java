package cn.thoughtworks.school;

import cn.thoughtworks.school.entities.Follow;
import cn.thoughtworks.school.repositories.FollowRepository;
import cn.thoughtworks.school.services.FlywayService;
import cn.thoughtworks.school.services.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Ignore
@SpringBootTest(classes = BackendApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureStubRunner(ids = "cn.thoughtworks.school.userCenter:backend:+:stubs:10003", repositoryRoot = "http://ec2-54-222-235-15.cn-north-1.compute.amazonaws.com.cn:8081/repository/maven-snapshots/", stubsMode = StubRunnerProperties.StubsMode.REMOTE)
@ActiveProfiles("test")
public class UserBase {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private FlywayService flywayService;

    @Autowired
    private FollowRepository followRepository;

    private static boolean cleanFlag = true;

    @Before
    public void setup() throws Exception {
        if (cleanFlag) {
            flywayService.migrateDatabase();
            Follow contactUser = new Follow((long) 1, (long) 2);
            followRepository.save(contactUser);
        }
        RestAssuredMockMvc.webAppContextSetup(wac);
    }
}
