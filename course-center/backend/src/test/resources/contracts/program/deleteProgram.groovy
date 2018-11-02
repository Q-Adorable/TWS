package program

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'DELETE'
        url value(consumer(regex('api/programs/\\d+')), producer('api/programs/2'))
    }
    response {
        status 204
    }
}
