package user

import org.springframework.cloud.contract.spec.Contract
Contract.make {
    request {
        method 'GET'
        url '/api/followees/searching?nameOrEmail=@qq'
        headers {
            header('id', value(consumer(regex('\\d+')), producer(1)))
        }
    }
    response {
        status 200
        body("""
                  [ 
                    {
                        "id": 1,
                        "username": "zhang",
                        "email": "zhang@qq.com",
                        "mobilePhone": "12345678901",
                        "followed": true
                    }
                  ]
                """)
        bodyMatchers {
            jsonPath('$.[*]',byType())
            jsonPath('$.[*].id', byRegex('\\d+'))
            jsonPath('$.[*].followed', byRegex('true|false'))
            jsonPath('$.[*].username', byRegex('[0-9a-zA-Z]+'))
            jsonPath('$.[*].email', byRegex('[0-9a-zA-Z]+@qq.com'))
            jsonPath('$.[*].mobilePhone', byRegex('[0-9a-zA-Z]+'))
        }
        headers {
            contentType(applicationJsonUtf8())
        }
    }
}
