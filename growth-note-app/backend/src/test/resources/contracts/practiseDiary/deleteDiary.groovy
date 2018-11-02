package practiseDiary

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'DELETE'
        url value(consumer(regex('/api/diaries/\\d+')), producer('/api/diaries/2'))
    }
    response {
        status 204
    }
}