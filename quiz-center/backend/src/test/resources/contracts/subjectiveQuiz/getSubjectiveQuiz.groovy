package contracts.subjectiveQuiz

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url value(consumer(regex('/api/v3/subjectiveQuizzes/[0-9]+')),producer('/api/v3/subjectiveQuizzes/1'))
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body("""
            {
                "id": 1,
                "description":"subjectiveQuizTest"
            }    
        """)
        testMatchers {
            jsonPath('$.id', byRegex(number()))
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}

