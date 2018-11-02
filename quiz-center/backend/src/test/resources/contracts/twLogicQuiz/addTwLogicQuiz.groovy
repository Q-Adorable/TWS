package twLogicQuiz

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url 'api/v3/logicQuizzes'
        body("""
              {    "hardCount":1,
                   "normalCount":2,
                   "easyCount":4
                   }
                """)
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 201
    }
}
