package program


import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/api/programs/my'
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
      "finishedTasks": [],
      "program": {
        "id": 1,
        "title": "program title",
        "createTime": "2016-09-09 00:00:00.0",
        "startTime": "2017-09-09 00:00:00.0",
        "endTime": "2017-09-09 00:00:00.0",
        "creatorId": 2,
        "category": "付费",
        "introduction": "program introduction"
      },
      "totalTasks": [
        {
          "id": 1,
          "programId": 1,
          "paperId": 1,
          "topicId": 1,
          "orderNumber": 1,
          "title": "task title",
          "content": "task content",
          "deadLine": "2017-09-09 00:00:00.0",
          "createTime": "2016-09-09 00:00:00.0",
          "visible": true,
          "type": "必修",
          "link": "http://www.baidu.com"
        }
      ]
    }
  ]
            """
        )
        bodyMatchers {
            jsonPath('$.[*]', byType())
            jsonPath('$.[*].program.id', byRegex(number()))
            jsonPath('$.[*].program.category', byRegex(onlyAlphaUnicode()))
            jsonPath('$.[*].totalTasks.[*].id', byRegex(number()))
            jsonPath('$.[*].totalTasks.[*].visible', byRegex(anyBoolean()))

        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}