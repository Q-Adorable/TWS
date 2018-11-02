package reviewQuiz

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method "get"
        url "api/v2/review/1"
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header("id",21)
        }
    }
    response {
        status 200
    }
}