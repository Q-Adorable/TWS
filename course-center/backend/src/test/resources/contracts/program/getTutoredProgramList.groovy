package program


import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/api/programs/tutored'
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header("id", 1)
        }
    }
    response {
        status 200
        body("""
[
    {
      "followStudents": [
        {
          "id": 1,
          "studentId": 1,
          "programId": 1
        }
      ],
      "totalStudents": [
        {
          "id": 1,
          "userId": 1,
          "programId": 1
        }
      ],
      "program": {
        "id": 1,
        "title": "program title",
        "createTime": "2016-09-09 00:00:00.0",
        "startTime": "2017-09-09 00:00:00.0",
        "endTime": "2017-09-09 00:00:00.0",
        "creatorId": 2,
        "category": "付费",
        "introduction": "program introduction"
      }
    }
  ]
  """)
        bodyMatchers {
            jsonPath('$.[*].followStudents.[*].id', byRegex(number()))
            jsonPath('$.[*].totalStudents.[*].id', byRegex(number()))
            jsonPath('$.[*].program.id', byRegex(number()))
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}