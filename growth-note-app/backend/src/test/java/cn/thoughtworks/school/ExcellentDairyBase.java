package cn.thoughtworks.school;

import cn.thoughtworks.school.entities.Comment;
import cn.thoughtworks.school.entities.ExcellentDiary;
import cn.thoughtworks.school.entities.PractiseDiary;
import cn.thoughtworks.school.repositories.CommentRepository;
import cn.thoughtworks.school.repositories.ExcellentDiaryRepository;
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

@Ignore
@SpringBootTest(classes = BackendApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureStubRunner(ids = "cn.thoughtworks.school.userCenter:backend:+:stubs:10003", repositoryRoot = "http://ec2-54-222-235-15.cn-north-1.compute.amazonaws.com.cn:8081/repository/maven-snapshots/",stubsMode = StubRunnerProperties.StubsMode.REMOTE)
@ActiveProfiles("test")
public class ExcellentDairyBase {

    @Autowired
    private ExcellentDiaryRepository excellentDiaryRepository;

    @Autowired
    private PractiseDiaryRepository practiseDiaryRepository;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    WebApplicationContext wac;
    @Autowired
    private FlywayService flywayService;

    private static boolean cleanFlag = true;

    @Before
    public void setup() throws Exception {
        if (cleanFlag) {
            flywayService.migrateDatabase();
            buildData();
            cleanFlag = false;
        }

        RestAssuredMockMvc.webAppContextSetup(wac);
    }

    private void buildData() {

        String createTime = "2018-01-20";
        Long diaryId = (long) 1;
        Long operatorId = (long) 1;
        excellentDiaryRepository.save(new ExcellentDiary(createTime, diaryId, operatorId));

        Long id = (long) 1;
        String createTime1 = "12:12:12";
        String date = "2018-01-01";
        String content = "content";
        Long authorId = (long) 1;
        practiseDiaryRepository.save(new PractiseDiary(id, createTime1, date, content, authorId));


        Comment comment = new Comment();
        comment.setCommentAuthorId(id);
        comment.setCommentContent("commentContent");
        comment.setCommentTime(createTime);
        comment.setPractiseDiaryId(id);
        comment.setId(id);
        commentRepository.save(comment);

    }
}
