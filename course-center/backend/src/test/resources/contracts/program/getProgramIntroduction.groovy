package program

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url value(consumer(regex('api/programs/\\d+/introductions')), producer('api/programs/1/introductions'))
    }
    response {
        status 200
        body("""
      {
        "id": 1,
        "title": "program title",
        "createTime": "2016-09-09 00:00:00.0",
        "startTime": "2017-09-09 00:00:00.0",
        "endTime": "2017-09-09 00:00:00.0",
        "creatorId": 2,
        "category": "付费",
        "introduction": "program introduction"
      }
""")
        bodyMatchers {
            jsonPath('$.id', byRegex(number()))
            jsonPath('$.category', byRegex(onlyAlphaUnicode()))

        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}
