package user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/api/followees'
        headers {
            header('id', value(consumer(regex('\\d+')), producer(1)))
        }
    }
    response {
        status 200
        body ("""
[
  {
    "userInfo": {
      "mobilePhone": "12345678901",
      "roles": [
        2
      ],
      "id": 1,
      "userName": "zhang",
      "email": "zhang@qq.com"
    },
    "practiseDiaryList": [
      {
        "id": 1,
        "createTime": "2012-12-12 00:00:00.0",
        "date": "2018-01-01 00:00:00.0",
        "content": "hfksdj",
        "authorId": 2
      }
    ]
  }
]
        """)
        bodyMatchers {
            jsonPath('$.[*].userInfo.id', byRegex('[0-9]+'))
            jsonPath('$.[*].practiseDiaryList.[*].id', byRegex('[0-9]+'))
        }
        headers{
            header('Content-Type': value(
                    producer(regex('application/json.*')),
                    consumer('application/json')
            ))
        }
    }
}