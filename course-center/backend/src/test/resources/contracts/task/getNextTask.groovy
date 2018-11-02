package task;

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method "GET"
        url value(consumer(regex('api/task/\\d+/flip')), producer('api/task/2/flip'))
    }
    response {
        status 200
        body("""
            {
                "nextId":-1,
                "previousId":-1
            }
        """)
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}