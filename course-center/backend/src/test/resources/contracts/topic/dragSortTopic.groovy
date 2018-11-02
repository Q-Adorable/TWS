package topic

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'PUT'
        url 'api/topics'
        body("""
                        [{
                          "id": 1,
                          "programId": 1,
                          "title": "title",
                          "createTime": "2016-09-09 00:00:00.0",
                          "orderNumber": 2,
                          "visible": true
                        }]
                   """)
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 204
    }
}