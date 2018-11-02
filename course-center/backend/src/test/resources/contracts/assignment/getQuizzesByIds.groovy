package assignment

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'get'
        url("/api/v2/assignments/quizzesByIds") {
            queryParameters {
                parameter('type', "SUBJECTIVE_QUIZ")
                parameter('ids', "1,2")
            }
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header('id', 21)
        }
    }
    response {
        status 200
        body("""
                [{
                   "id": 1,
                   "description": "主观题"
               }]""")
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
        bodyMatchers {
            jsonPath('$.[*]', byType())
            jsonPath('$.[*].id', byRegex(number()))
            jsonPath('$.[*].description', byRegex(nonEmpty()))
        }
    }
}