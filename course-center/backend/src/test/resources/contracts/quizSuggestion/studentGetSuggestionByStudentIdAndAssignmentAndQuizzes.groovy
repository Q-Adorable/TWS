package quizSuggestion

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'get'
        url '/api/v2/assignments/1/quizzes/1/suggestions'
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header('id', 21)
        }
    }
    response {
        status 200
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
        bodyMatchers {
            jsonPath('$.[*].studentId', byRegex(number()))
            jsonPath('$.[*].assignmentId', byRegex(number()))
            jsonPath('$.[*].quizId', byRegex(number()))

        }
    }
}