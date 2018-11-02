package contracts.excellentDairy

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method "POST"
        url '/api/excellentDiaries'
        body("""
            {
            "diaryId":2,
            "id":2,
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