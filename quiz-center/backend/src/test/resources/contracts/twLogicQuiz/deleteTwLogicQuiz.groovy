package twLogicQuiz

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'DELETE'
        url value(consumer(regex('/api/v3/logicQuizzes/[0-9]+')), producer('/api/v3/logicQuizzes/1'))
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 204
    }
}
