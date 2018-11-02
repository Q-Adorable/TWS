package contracts.subjectiveQuiz

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'DELETE'
        url value(consumer(regex('/api/v3/subjectiveQuizzes/[0-9]+')), producer('/api/v3/subjectiveQuizzes/2'))
headers {
    header('Content-Type', 'application/json;charset=UTF-8')
}
}
response {
    status 204
}
}
