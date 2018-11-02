package quizSuggestion

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'put'
        url '/api/v2/suggestions/1'
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header('id', 1)
        }
        body("""
            {
                "id": 1,
                "content": "content",
            }""")
    }
    response {
        status 204
    }
}