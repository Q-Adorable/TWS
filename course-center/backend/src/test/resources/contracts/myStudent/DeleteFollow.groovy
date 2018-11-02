package myStudent

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'DELETE'
        url value(consumer(regex('/api/myStudents/programs/[0-9]+/followers/[0-9]+')), producer('/api/myStudents/programs/1/followers/2'))
        headers {
            header("id", 1)
        }
    }
    response {
        status 204
    }
}