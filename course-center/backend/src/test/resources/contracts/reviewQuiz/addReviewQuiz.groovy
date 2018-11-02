package reviewQuiz

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method "post"
        url "api/v2/review"
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header("id",21)
        }
        body("""{
            "status":"已完成" 
            }
        """)
    }
    response {
        status 201
        bodyMatchers {
            jsonPath('$.id', byRegex(number()))
        }
    }
}