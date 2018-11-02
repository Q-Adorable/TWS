package task;

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url value(consumer(regex('api/topics/[0-9]+')), producer('api/topics/1'))
    }
    response {
        status 200
        body("""
                {
                   "topic": {
                          "id": 1,
                          "programId": 1,
                          "title": "title",
                          "createTime": "2018-01-01 00:00:00.0",
                          "orderNumber": 1,
                          "visible": true
                        },
                    "tasks":[{
                            "id":1,
                            "programId": 1,
                            "paperId": 1,
                            "topicId": 1,
                            "orderNumber":1,
                            "title":"title",
                            "content":"content",
                            "deadLine":"2018-01-17 00:00:00.0",
                            "createTime":"2018-01-01 00:00:00.0",
                            "visible":true,
                            "type":"type",
                            "link":"link"
                    }]
                }
                """)
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}