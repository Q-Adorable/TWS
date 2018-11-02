package task;

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method "PUT"
        url value(consumer(regex('api/tasks/[0-9]+')), producer('api/tasks/1'))
        body("""
            {
              "id":1,
              "programId":1,
              "title":"title",
              "createTime":"2018-01-01 00:00:00.0", 
              "orderNumber":1
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