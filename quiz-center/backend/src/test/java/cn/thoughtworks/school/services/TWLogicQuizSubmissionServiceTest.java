package cn.thoughtworks.school.services;

import cn.thoughtworks.school.Application;
import cn.thoughtworks.school.entities.TWLogicQuizSubmission;
import cn.thoughtworks.school.handlers.BusinessException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class TWLogicQuizSubmissionServiceTest {

    @Autowired
    TWLogicQuizSubmissionService submissionService;

//    @Test
//    public void should_return_true() throws Exception {
//        TWLogicQuizSubmission submission = submissionService.findByAssignmentAndUserId(1L, 1L);
//        assertThat(true).isEqualTo(true);
//    }
}
