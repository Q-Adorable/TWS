package basicQuiz

import org.springframework.cloud.contract.spec.Contract

[Contract.make {
    request {
        method 'DELETE'
        url value(consumer(regex('/api/v3/basicQuizzes/[0-9]+')), producer('/api/v3/basicQuizzes/2'))
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 204
    }
}]

