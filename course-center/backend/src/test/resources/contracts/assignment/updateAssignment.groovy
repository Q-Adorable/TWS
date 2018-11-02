package assignment

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'put'
        url value(consumer("api/v2/assignments/\\d+"),
                producer("api/v2/assignments/1"))
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header('id', 21)
        }
        body("""{
                "type":"BASIC_QUIZ",
                "title":"new title"
                }
        """)
    }
    response {
        status 204
    }
}