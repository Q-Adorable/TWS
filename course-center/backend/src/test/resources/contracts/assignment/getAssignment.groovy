package assignment

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'get'
        url value(consumer("api/v2/assignments/\\d+"),
                producer("api/v2/assignments/1"))
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header('id', 21)
        }
    }
    response {
        status 200
        body("""
                {
                    "id":1,
                    "taskId":1,
                    "type":"主观题",
                    "title":"test",
                    "creatorId":21
               }""")
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
        bodyMatchers {
            jsonPath('$.[*]', byType())
            jsonPath('$.id', byRegex(number()))
            jsonPath('$.taskId', byRegex(number()))
            jsonPath('$.type', byRegex(nonEmpty()))
            jsonPath('$.title', byRegex(nonEmpty()))
            jsonPath('$.creatorId', byRegex(number()))
        }
    }
}