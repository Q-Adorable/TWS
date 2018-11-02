package quizSuggestion

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'post'
        url '/api/v2/suggestions'
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header('id', 1)
        }
        body("""
            {
                "studentId": 1,
                "content": "content",
                "assignmentId": 1,
                "quizId": 1,
            }""")
    }
    response {
        status 201
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}