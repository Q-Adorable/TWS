package user

import org.springframework.cloud.contract.spec.Contract

Contract.make{
    request {
        method 'GET'
        url value(consumer(regex('/api/followees/[0-9]+/practise-diaries')), producer('/api/followees/2/practise-diaries'))
        headers {
            header('id', value(consumer(regex('\\d+')), producer(1)))
        }
    }
    response {
        status 200
        body("""
{
  "followeeInfo": {
    "mobilePhone": "12345678901",
    "roles": [
      2
    ],
    "id": 1,
    "userName": "zhang",
    "email": "zhang@qq.com"
  },
  "total": 1,
  "practiseDiaryAndComments": [
    {
      "comments": [],
      "practiseDiary": {
        "id": 1,
        "createTime": "2012-12-12 00:00:00.0",
        "date": "2018-01-01 00:00:00.0",
        "content": "hfksdj",
        "authorId": 2
      }
    }
  ]
}
        """)
        bodyMatchers {
            jsonPath('$.followeeInfo.id', byRegex(number()))
            jsonPath('$.total', byRegex(number()))
            jsonPath('$.practiseDiaryAndComments.[*].practiseDiary.id', byRegex(number()))
            jsonPath('$.practiseDiaryAndComments.[*].practiseDiary.authorId', byRegex(number()))
        }
        headers {
            header('Content-Type': value(
                    producer(regex('application/json.*')),
                    consumer('application/json')
            ))
        }
    }
}