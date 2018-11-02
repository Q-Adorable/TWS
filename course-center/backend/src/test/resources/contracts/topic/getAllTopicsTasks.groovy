package topic

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url value(consumer(regex('api/programs/\\d+/topicTask')), producer('api/programs/1/topicTask'))
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body("""
[
    {
      "topics": {
        "id": 1,
        "programId": 1,
        "title": "topic title",
        "createTime": "2016-09-09 00:00:00.0",
        "orderNumber": 1,
        "visible": true
      },
      "tasks": [
        {
          "id": 1,
          "programId": 1,
          "paperId": 1,
          "topicId": 1,
          "orderNumber": 1,
          "title": "task title",
          "content": "task content",
          "deadLine": "2018-01-17 00:00:00.0",
          "createTime": "2016-09-09 00:00:00.0",
          "visible": true,
          "type": "type",
          "link": "link"
        }
      ]
    }
  ]
""")
        bodyMatchers {
            jsonPath('$.[*].topics.id', byRegex(number()))
            jsonPath('$.[*].tasks.[*].id', byRegex(number()))
            jsonPath('$.[*].tasks.[*].programId', byRegex(number()))
            jsonPath('$.[*].tasks.[*].visible', byRegex(anyBoolean()))
        }
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}