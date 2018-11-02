package task;

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method "PUT"
        url "api/tasks"
        body("""
            [{
              
              "programId":1,
              "title":"title",
              "createTime":"2018-01-01 00:00:00.0",
              "orderNumber":1
            },{
              
              "programId":2,
              "title":"title2",
              "createTime":"2018-01-02 00:00:00.0",
              "orderNumber":2
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