package assignment

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'delete'
        url value(consumer("api/v2/assignments/\\d+"),
                producer("api/v2/assignments/2"))
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header('id', 21)
        }
    }
    response {
        status 204
    }
}