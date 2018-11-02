package cn.thoughtworks.school;

import cn.thoughtworks.school.services.FlywayService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

@Ignore
@SpringBootTest(classes = BackendApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class CommentBase {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private FlywayService flywayService;

    private static boolean cleanFlag = true;

    @Before
    public void setup() throws Exception{
        if (cleanFlag) {
            flywayService.migrateDatabase();
            cleanFlag = false;
        }

        RestAssuredMockMvc.webAppContextSetup(wac);
    }
}
