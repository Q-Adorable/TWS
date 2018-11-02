package program

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/api/programs'
    }
    response {
        status 200
        body("""
    {
    "content": [
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
    ],
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "first": true,
    "sort": [
      {
        "direction": "DESC",
        "property": "id",
        "ignoreCase": false,
        "nullHandling": "NATIVE",
        "ascending": false,
        "descending": true
      }
    ],
    "numberOfElements": 1,
    "size": 10,
    "number": 0
    }
""")
        bodyMatchers {
            jsonPath('$.[*]', byType())
            jsonPath('$.content.[*].id', byRegex(number()))
            jsonPath('$.content.[*].category', byRegex(onlyAlphaUnicode()))
            jsonPath('$.totalPages', byRegex(number()))
            jsonPath('$.totalElements', byRegex(number()))
            jsonPath('$.numberOfElements', byRegex(number()))
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}