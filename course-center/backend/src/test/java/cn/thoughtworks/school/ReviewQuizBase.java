package cn.thoughtworks.school;

import cn.thoughtworks.school.programCenter.Application;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
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
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@DBRider
@DBUnit(caseSensitiveTableNames = true)
@DataSet("reviewQuiz.yml")
@ActiveProfiles("test")
public class ReviewQuizBase {
    @Autowired
    WebApplicationContext wac;

    @Before
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(wac);
    }
}

