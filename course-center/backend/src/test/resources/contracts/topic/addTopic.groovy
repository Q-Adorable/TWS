package topic

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url 'api/topics'
        body("""
                   {
                     "programId": 1,
                     "title": "title",
                     "createTime": "2016-09-09 00:00:00.0",
                     "orderNumber": 1,
                     "visible": true
                   }
                """)
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 201
    }
}