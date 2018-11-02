package subjectiveQuiz

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/api/v3/subjectiveQuizzes/selecting/1,2'
}
    response {
        status 200
        body("""
            [{
            "id": 1,
            "description":"subjectiveQuiz"
             },
             {
            "id": 2,
            "description":"subjectiveQuiz2"
             }]
        """)
        testMatchers {
            jsonPath('$.[*].id', byRegex(number()))
            jsonPath('$.[*].description', byRegex(nonEmpty()))
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}