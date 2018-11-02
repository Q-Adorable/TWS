package taskFeeling

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'get'
        url value(consumer(regex('/api/tasks/\\d+/feelings')), producer('/api/tasks/1/feelings'))
        body([
                taskId: 1
        ])
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header("id", 1)
        }
    }
    response {
        status 200
    }
}