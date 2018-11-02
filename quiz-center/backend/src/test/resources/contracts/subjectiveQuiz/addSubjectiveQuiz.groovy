package contracts.subjectiveQuiz
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/api/v3/subjectiveQuizzes'
        body("""
                   {
                     "quizGroupId": 1,
                     "description": "title",
                     "createTime": "2016-09-09 00:00:00.0",
                     "tags":[]
                   }
                """)
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header('id', 1)
        }
    }
    response {
        status 201
    }
}

