package basicQuiz

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url 'api/v3/basicQuizzes'
        body("""
                 {
                    "answer":["1","3"],
                        "type":"MULTIPLE_CHOICE",
                    "description":"12304",
                    "choices": ["kdgfj", "1", "3", "4"],
                    "tags": ["js", "jb"],
                    "quizGroupId": "1"
                 } 
                """)
        headers {
            header('id', 1)
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 201
        body("""
            {
                "id": 4,
                "uri": "/api/v3/basicQuizzes/4"
            }
        """)
        testMatchers {
            jsonPath('$.id', byRegex(number()))
            jsonPath('$.uri', byRegex("/api/v3/basicQuizzes/\\d+"))
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}
