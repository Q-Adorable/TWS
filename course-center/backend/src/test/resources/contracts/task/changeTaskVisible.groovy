package task;

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method "PUT"
        url value(consumer(regex('api/tasks/\\d+/visibility')), producer('api/tasks/1/visibility'))
        body("""
             {
                "visibility":false
             }
        """)
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 204
    }
}