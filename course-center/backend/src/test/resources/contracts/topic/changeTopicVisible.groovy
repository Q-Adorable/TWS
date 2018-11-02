package topic

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'PUT'
        url value(consumer(regex('api/topics/\\d+/visibility')), producer('api/topics/1/visibility'))
        body("""
                        {
                          "id": 1,
                          "programId": 1,
                          "title": "title",
                          "createTime": "2016-09-09 00:00:00.0",
                          "orderNumber": 1,
                          "visible": false
                        }
                   """)
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 204
    }
}