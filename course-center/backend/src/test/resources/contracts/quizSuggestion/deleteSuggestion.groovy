package quizSuggestion

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'delete'
        url '/api/v2/suggestions/2'
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header('id', 1)
        }
    }
    response {
        status 204
    }
}