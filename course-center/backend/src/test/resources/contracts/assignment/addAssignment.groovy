package assignment

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'post'
        url '/api/v2/assignments'
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header('id', 1)
        }
        body([
                "taskId": 1,
                "type"  : 'BASIC_QUIZ',
                "title" : 'title'
        ])
    }
    response {
        status 201
        body([
                uri: "/api/v2/assignments/1",
                id : 1
        ])
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
        bodyMatchers {
            jsonPath('$.*', byType())
            jsonPath('$.id', byRegex(number()))
            jsonPath('$.uri', byRegex(nonBlank()))
        }
    }
}