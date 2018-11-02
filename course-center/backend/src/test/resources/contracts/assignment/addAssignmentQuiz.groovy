package assignment

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'post'
        url '/api/v2/tasks/1/assignments/1/quizzes'
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header('id', 1)
        }
        body([
                1,2,3
        ])
    }
    response {
        status 204
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}