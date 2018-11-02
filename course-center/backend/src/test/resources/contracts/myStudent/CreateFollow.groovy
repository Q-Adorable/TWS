package myStudent

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url value(consumer(regex('/api/myStudents/programs/[0-9]+/followers/[0-9]+')), producer('/api/myStudents/programs/1/followers/1'))
        headers {
            header("id", 1)
        }
    }
    response {
        status 201
    }
}