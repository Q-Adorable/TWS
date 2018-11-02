package program

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url value(consumer(regex('api/programs/\\d+/student/\\d+')), producer('api/programs/1/student/1'))
        body ([
                programId: 1,
                studentId: 1
        ])
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 201
    }
}