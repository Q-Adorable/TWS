package contracts.user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url('/api/users') {
            queryParameters {
                parameter("username", "zhang")
            }
        }
        headers {
            header('id', value(consumer(regex('\\d+')), producer(1)))
        }
    }
    response {
        status 200
        body("""
[{
  "mobilePhone": "12345678901",
  "roles": [
    2
  ],
  "id": 1,
  "userName": "zhang",
  "email": "zhang@qq.com"
}]
        """)
        bodyMatchers {
            jsonPath('$[*]',byType())
            jsonPath('$[*].id', byRegex(number()))
            jsonPath('$[*].mobilePhone', byRegex(number()))
            jsonPath('$[*].userName', byRegex("[\\s\\S]+"))
            jsonPath('$[*].email', byRegex(email()))
            jsonPath('$[*].roles', byType())
        }
        headers {
            header('Content-Type': value(
                    producer(regex('application/json.*')),
                    consumer('application/json')
            ))
//            contentType(applicationJson())
        }
    }
}