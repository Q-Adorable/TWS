package user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url value(consumer(regex('/api/followees/[0-9]+')), producer('/api/followees/1'))
        headers {
            header('id', value(consumer(regex('\\d+')), producer(1)))
        }
    }
    response {
        status 201
        body value(consumer(10), producer(regex('[0-9]+')))

        headers{
            header('Content-Type': value(
                    producer(regex('application/json.*')),
                    consumer('application/json')
            ))
        }
    }
}