package practiseDiary

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/api/diaries'
        body("""
        {
        "createTime":"12:12:12",
        "data":"2018-01-01",
        "content":"content",
        "authorId":1
        }
        """)
        headers {
            contentType(applicationJsonUtf8())
            header('id', value(consumer(regex('\\d+')), producer(1)))
        }
    }
    response {
        status 201
    }
}