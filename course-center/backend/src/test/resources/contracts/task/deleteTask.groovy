package task;

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method "DELETE"
        url value(consumer(regex('api/tasks/[0-9]+')), producer('api/tasks/1'))
    }
    response {
        status 204
    }
}