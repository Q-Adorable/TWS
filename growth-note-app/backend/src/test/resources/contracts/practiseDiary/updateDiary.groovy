package practiseDiary

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'PUT'
        url value(consumer(regex('/api/diaries/\\d+')), producer('/api/diaries/1'))
        body("""
        {
        "createTime":"12:12:12",
        "data":"2018-01-01",
        "content":"update content",
        "authorId":1
        }
        """)
        headers {
            contentType(applicationJsonUtf8())
        }
    }
    response {
        status 204
    }
}