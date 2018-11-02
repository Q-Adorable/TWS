package basicQuiz

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'PUT'
        url value(consumer(regex('/api/v3/basicQuizzes/[0-9]+')), producer('/api/v3/basicQuizzes/2'))
        body("""
                 {
                    "id": 4,
                    "answer":["1", "3"],
                    "type":"MULTIPLE_CHOICE",
                    "description":"12304",
                    "choices": ["kdgfj", "1", "3", "4"],
                    "tags": ["js", "jb"]
                 } 
                """)
        headers {
            header('id', 1)
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 204
    }
}

