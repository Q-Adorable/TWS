package excellentDairy

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method "DELETE"
        url value(consumer(regex('/api/excellentDiaries/\\d+')), producer('/api/excellentDiaries/1'))

    }
    response {
        status 204
    }
}