package contracts.subjectiveQuiz
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'PUT'
        url value(consumer(regex('/api/v3/subjectiveQuizzes/[0-9]+')), producer('/api/v3/subjectiveQuizzes/1'))
        body("""
                 {
                    "id": 2,
                    "description":"12304",
                    "tags": ["js"]
                 } 
                """)
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 204
    }
}

