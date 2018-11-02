package cn.thoughtworks.school;

import cn.thoughtworks.school.entities.Follow;
import cn.thoughtworks.school.entities.PractiseDiary;
import cn.thoughtworks.school.repositories.FollowRepository;
import cn.thoughtworks.school.repositories.PractiseDiaryRepository;
import cn.thoughtworks.school.services.FlywayService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

@Ignore
@SpringBootTest(classes = BackendApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureStubRunner(ids = "cn.thoughtworks.school.userCenter:backend:+:stubs:10003", repositoryRoot = "http://ec2-54-222-235-15.cn-north-1.compute.amazonaws.com.cn:8081/repository/maven-snapshots/",stubsMode = StubRunnerProperties.StubsMode.REMOTE)
@ActiveProfiles("test")
public class FollowBase {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private FlywayService flywayService;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private PractiseDiaryRepository practiseDiaryRepository;

    private static boolean cleanFlag = true;

    @Before
    public void setup() throws Exception{
        if (cleanFlag) {
            flywayService.migrateDatabase();
            cleanFlag = false;
            Follow contactUser = new Follow((long)1, (long)2);
            followRepository.save(contactUser);
            String createTime = "12:12:12";
            String date = "2018-01-01";
            String content = "hfksdj";
            Long authorId = (long)2;
            PractiseDiary practiseDiary = new PractiseDiary(createTime, date, content, authorId);
            practiseDiaryRepository.save(practiseDiary);
        }

        RestAssuredMockMvc.webAppContextSetup(wac);
    }
}
