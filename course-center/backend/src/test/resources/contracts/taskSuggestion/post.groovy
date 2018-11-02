package taskSuggestion

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url value(consumer(regex('api/tasks/\\d+/suggestions')), producer('api/tasks/1/suggestions'))
        body("test")
        headers {
            header('id', 1)
        }
    }
    response {
        status 201
        headers {
        }
    }
}