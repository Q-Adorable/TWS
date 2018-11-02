package cn.thoughtworks.school.services;

import cn.thoughtworks.school.entities.LogicQuiz;
import cn.thoughtworks.school.repositories.LogicQuizRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LogicQuizServiceTest {

    @InjectMocks
    LogicQuizItemService logicQuizService;

    @Mock
    LogicQuizRepository logicQuizRepository;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_return_true() {
        LogicQuiz logicQuiz = new LogicQuiz();
        logicQuiz.setId(2L);

        Mockito.when(logicQuizRepository.findByIdAndIsAvailableIsTrue(1L)).thenReturn(logicQuiz);

        Integer count = logicQuizService.getCount();
        assertThat(count).isEqualTo(10);
    }
}
