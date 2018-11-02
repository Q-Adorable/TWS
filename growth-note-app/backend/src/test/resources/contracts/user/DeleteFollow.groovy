package user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'DELETE'
        url value(consumer(regex('/api/followees/[0-9]+')), producer('/api/followees/2'))
        headers {
            header('id', value(consumer(regex('\\d+')), producer(1)))
        }
    }
    response {
        status 204
    }
}