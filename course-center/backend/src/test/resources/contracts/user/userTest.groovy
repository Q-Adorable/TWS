package user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url value(consumer(regex('api/users/\\d+')), producer('api/users/1'))
    }
    response {
        status 200
        body("""
                  {
                          "id": 1,
                          "userName": "zhang",
                          "email": "zhang@qq.com",
                          "mobilePhone": "12345678901",
                          "roles": [2]
                  }
                """)
        bodyMatchers {
            jsonPath('$.id', byRegex(number()))
            jsonPath('$.userName', byRegex(nonEmpty()))
            jsonPath('$.email', byRegex(email()))
            jsonPath('$.mobilePhone', byRegex(number()))
            jsonPath('$.roles', byType())
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}
