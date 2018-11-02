package cn.thoughtworks.school.contract;

import cn.thoughtworks.school.Application;
import cn.thoughtworks.school.entities.Tag;
import cn.thoughtworks.school.repositories.TagRepository;
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
public class TagBase {
    @Autowired
    WebApplicationContext wac;
    @Autowired
    TagRepository tagRepository;

    @Before
    public void setup() throws Exception {
        initData();
        RestAssuredMockMvc.webAppContextSetup(wac);
    }

    private void initData() {
        Tag tag = new Tag("first", (long) 1, 0L);
        Tag tagTwo = new Tag("second", (long) 1, 0L);
        tagRepository.save(tag);
        tagRepository.save(tagTwo);
    }
}
